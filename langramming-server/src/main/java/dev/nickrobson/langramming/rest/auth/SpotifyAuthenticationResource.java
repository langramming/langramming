package dev.nickrobson.langramming.rest.auth;

import dev.nickrobson.langramming.client.spotify.SpotifyRestClient;
import dev.nickrobson.langramming.manager.BaseUrlManager;
import dev.nickrobson.langramming.manager.SpotifyUserManager;
import dev.nickrobson.langramming.util.LangrammingClientError;
import dev.nickrobson.langramming.util.ResponseHelper;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

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

    private final BaseUrlManager baseUrlManager;
    private final SpotifyRestClient spotifyRestClient;
    private final SpotifyUserManager spotifyUserManager;
    private final ResponseHelper responseHelper;

    @Inject
    public SpotifyAuthenticationResource(
        BaseUrlManager baseUrlManager,
        SpotifyRestClient spotifyRestClient,
        SpotifyUserManager spotifyUserManager,
        ResponseHelper responseHelper
    ) {
        this.baseUrlManager = baseUrlManager;
        this.spotifyRestClient = spotifyRestClient;
        this.spotifyUserManager = spotifyUserManager;
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
        throws IOException, SpotifyWebApiException, ParseException {
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

        spotifyUserManager.createOrUpdateUser(authorizationCodeCredentials);

        return responseHelper.redirect(baseUrlManager.getBaseUrl());
    }
}
