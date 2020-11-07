package dev.nickrobson.langramming.rest.auth;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import dev.nickrobson.langramming.client.spotify.SpotifyRestClient;
import dev.nickrobson.langramming.service.BaseUrlService;
import dev.nickrobson.langramming.service.SpotifyUserService;
import dev.nickrobson.langramming.util.LangrammingClientError;
import dev.nickrobson.langramming.util.ResponseHelper;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/spotify")
public class SpotifyAuthenticationResource {
    private static final String SPOTIFY_SCOPES = String.join(
        ",",
        "playlist-read-collaborative",
        "playlist-read-private",
        "user-follow-read",
        "user-read-recently-played",
        "user-top-read"
    );

    private final BaseUrlService baseUrlService;
    private final SpotifyRestClient spotifyRestClient;
    private final SpotifyUserService spotifyUserService;
    private final ResponseHelper responseHelper;

    @Inject
    public SpotifyAuthenticationResource(
        BaseUrlService baseUrlService,
        SpotifyRestClient spotifyRestClient,
        SpotifyUserService spotifyUserService,
        ResponseHelper responseHelper
    ) {
        this.baseUrlService = baseUrlService;
        this.spotifyRestClient = spotifyRestClient;
        this.spotifyUserService = spotifyUserService;
        this.responseHelper = responseHelper;
    }

    @GetMapping("/connect")
    public ResponseEntity<?> connectSpotify() {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyRestClient
            .getUnauthenticatedSpotifyApi()
            .authorizationCodeUri()
            .scope(SPOTIFY_SCOPES)
            .show_dialog(false)
            .build();

        URI uri = authorizationCodeUriRequest.execute();
        return responseHelper.redirect(uri);
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> onReturnFromSpotify(HttpServletRequest httpServletRequest)
        throws IOException, SpotifyWebApiException {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        Optional<String> errorOpt = Optional.ofNullable(httpServletRequest.getParameter("error"));
        Optional<String> codeOpt = Optional.ofNullable(httpServletRequest.getParameter("code"));

        if (codeOpt.isEmpty()) {
            if (errorOpt.isPresent()) {
                String error = errorOpt.get();
                if ("access_denied".equals(error)) {
                    return responseHelper.redirectToError(
                        LangrammingClientError.SPOTIFY_ACCESS_DENIED
                    );
                }
            }
            return responseHelper.redirectToError(LangrammingClientError.SPOTIFY_UNKNOWN_ERROR);
        }

        String code = codeOpt.get();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyRestClient
            .getUnauthenticatedSpotifyApi()
            .authorizationCode(code)
            .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        spotifyUserService.createOrUpdateUser(authorizationCodeCredentials);

        return responseHelper.redirect(baseUrlService.getBaseUrl());
    }
}
