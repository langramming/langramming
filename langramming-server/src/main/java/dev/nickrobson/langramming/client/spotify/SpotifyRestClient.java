package dev.nickrobson.langramming.client.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import dev.nickrobson.langramming.configuration.LangrammingSpotifyConfiguration;
import dev.nickrobson.langramming.manager.BaseUrlManager;
import dev.nickrobson.langramming.manager.SpotifyUserManager;
import dev.nickrobson.langramming.model.SpotifyUser;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpotifyRestClient {

    private final BaseUrlManager baseUrlManager;
    private final LangrammingSpotifyConfiguration spotifyConfiguration;
    private final SpotifyUserManager spotifyUserManager;

    @Inject
    public SpotifyRestClient(
        BaseUrlManager baseUrlManager,
        SpotifyUserManager spotifyUserManager,
        LangrammingSpotifyConfiguration spotifyConfiguration
    ) {
        this.baseUrlManager = baseUrlManager;
        this.spotifyUserManager = spotifyUserManager;
        this.spotifyConfiguration = spotifyConfiguration;
    }

    private SpotifyApi.Builder spotifyApiBuilder() {
        return SpotifyApi
            .builder()
            .setClientId(spotifyConfiguration.getClientId())
            .setClientSecret(spotifyConfiguration.getClientSecret())
            .setRedirectUri(getRedirectUri());
    }

    public SpotifyApi getUnauthenticatedSpotifyApi() {
        return spotifyApiBuilder().build();
    }

    public SpotifyApi getAuthenticatedSpotifyApi() {
        Optional<SpotifyUser> spotifyUserOpt = spotifyUserManager.getCurrentSpotifyUser();
        SpotifyUser spotifyUser = spotifyUserOpt.orElseThrow(
            () -> new IllegalStateException("Expected logged in user")
        );

        if (spotifyUser.getExpiresAt() <= Instant.now().getEpochSecond()) {
            try {
                AuthorizationCodeCredentials credentials = spotifyApiBuilder()
                    .build()
                    .authorizationCodeRefresh()
                    .refresh_token(spotifyUser.getRefreshToken())
                    .build()
                    .execute();

                spotifyUser =
                    spotifyUserManager.createOrUpdateUser(
                        new AuthorizationCodeCredentials.Builder()
                            .setScope(credentials.getScope())
                            .setTokenType(credentials.getTokenType())
                            .setExpiresIn(credentials.getExpiresIn())
                            .setAccessToken(credentials.getAccessToken())
                            .setRefreshToken(spotifyUser.getRefreshToken())
                            .build()
                    );
            } catch (SpotifyWebApiException | IOException ex) {
                ex.printStackTrace();
                throw new IllegalStateException("Failed to refresh Spotify token", ex);
            }
        }

        return spotifyApiBuilder()
            .setAccessToken(spotifyUser.getAccessToken())
            .setRefreshToken(spotifyUser.getRefreshToken())
            .build();
    }

    private URI getRedirectUri() {
        String serverUrl = baseUrlManager.getBaseUrl();
        String baseUrl = serverUrl + (serverUrl.endsWith("/") ? "" : "/");
        return SpotifyHttpManager.makeUri(baseUrl + "api/auth/spotify/redirect");
    }
}
