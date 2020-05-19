package com.github.langramming.rest.auth;

import com.github.langramming.client.spotify.SpotifyRestClient;
import com.github.langramming.service.SpotifyUserService;
import com.github.langramming.util.LangrammingClientError;
import com.github.langramming.util.ResponseHelper;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/spotify")
public class SpotifyAuthenticationResource {
    private static final String SPOTIFY_SCOPES = String.join(
            ",",
            "playlist-read-collaborative",
            "playlist-read-private",
            "user-follow-read",
            "user-read-recently-played",
            "user-top-read");

    private final SpotifyRestClient spotifyRestClient;
    private final SpotifyUserService spotifyUserService;
    private final ResponseHelper responseHelper;

    @Inject
    public SpotifyAuthenticationResource(
            SpotifyRestClient spotifyRestClient,
            SpotifyUserService spotifyUserService,
            ResponseHelper responseHelper) {
        this.spotifyRestClient = spotifyRestClient;
        this.spotifyUserService = spotifyUserService;
        this.responseHelper = responseHelper;
    }

    @GetMapping("/connect")
    public ResponseEntity<?> connectSpotify() {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyRestClient.getSpotifyApi()
                .authorizationCodeUri()
                .scope(SPOTIFY_SCOPES)
                .show_dialog(false)
                .build();

        URI uri = authorizationCodeUriRequest.execute();
        return responseHelper.redirect(uri);
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> onReturnFromSpotify(HttpServletRequest httpServletRequest) throws IOException, SpotifyWebApiException {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        Optional<String> errorOpt = Optional.ofNullable(httpServletRequest.getParameter("error"));
        Optional<String> codeOpt = Optional.ofNullable(httpServletRequest.getParameter("code"));

        if (codeOpt.isEmpty()) {
            if (errorOpt.isPresent()) {
                String error = errorOpt.get();
                if ("access_denied".equals(error)) {
                    return responseHelper.redirectToError(LangrammingClientError.SPOTIFY_ACCESS_DENIED);
                }
            }
            return responseHelper.redirectToError(LangrammingClientError.SPOTIFY_UNKNOWN_ERROR);
        }

        String code = codeOpt.get();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyRestClient.getSpotifyApi()
                .authorizationCode(code)
                .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        spotifyUserService.createOrUpdateUser(authorizationCodeCredentials);

        return responseHelper.redirect("/");
    }
}
