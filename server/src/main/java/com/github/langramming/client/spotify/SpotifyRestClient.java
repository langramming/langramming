package com.github.langramming.client.spotify;

import com.github.langramming.configuration.LangrammingServerConfiguration;
import com.github.langramming.configuration.LangrammingSpotifyConfiguration;
import com.github.langramming.model.SpotifyUser;
import com.github.langramming.service.SpotifyUserService;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@Singleton
public class SpotifyRestClient {

    private final LangrammingServerConfiguration serverConfiguration;
    private final LangrammingSpotifyConfiguration spotifyConfiguration;
    private final SpotifyUserService spotifyUserService;

    @Inject
    public SpotifyRestClient(
            LangrammingServerConfiguration serverConfiguration,
            LangrammingSpotifyConfiguration spotifyConfiguration, SpotifyUserService spotifyUserService) {
        this.serverConfiguration = serverConfiguration;
        this.spotifyConfiguration = spotifyConfiguration;
        this.spotifyUserService = spotifyUserService;
    }

    private SpotifyApi.Builder spotifyApiBuilder() {
        return SpotifyApi.builder()
                .setClientId(spotifyConfiguration.getClientId())
                .setClientSecret(spotifyConfiguration.getClientSecret())
                .setRedirectUri(getRedirectUri(serverConfiguration.getUrl()));
    }

    public SpotifyApi getUnauthenticatedSpotifyApi() {
        return spotifyApiBuilder().build();
    }

    public SpotifyApi getAuthenticatedSpotifyApi() {
        Optional<SpotifyUser> spotifyUserOpt = spotifyUserService.getCurrentSpotifyUser();
        SpotifyUser spotifyUser = spotifyUserOpt.orElseThrow(() -> new IllegalStateException("Expected logged in user"));

        if (spotifyUser.getExpiresAt() <= Instant.now().getEpochSecond()) {
            try {
                AuthorizationCodeCredentials authorizationCodeCredentials = spotifyApiBuilder().build()
                        .authorizationCodeRefresh()
                        .refresh_token(spotifyUser.getRefreshToken())
                        .build()
                        .execute();

                spotifyUser = spotifyUserService.createOrUpdateUser(authorizationCodeCredentials);
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

    private static URI getRedirectUri(String serverUrl) {
        String baseUrl = serverUrl + (serverUrl.endsWith("/") ? "" : "/");
        return SpotifyHttpManager.makeUri(baseUrl + "api/auth/spotify/redirect");
    }

}
