package com.github.langramming.client.spotify;

import com.github.langramming.configuration.LangrammingServerConfiguration;
import com.github.langramming.configuration.LangrammingSpotifyConfiguration;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;

@Singleton
public class SpotifyRestClient {

    @Getter
    private final SpotifyApi spotifyApi;

    @Inject
    public SpotifyRestClient(
            LangrammingServerConfiguration serverConfiguration,
            LangrammingSpotifyConfiguration spotifyConfiguration) {
        this.spotifyApi = SpotifyApi.builder()
                .setClientId(spotifyConfiguration.getClientId())
                .setClientSecret(spotifyConfiguration.getClientSecret())
                .setRedirectUri(getRedirectUri(serverConfiguration.getUrl()))
                .build();
    }

    private URI getRedirectUri(String serverUrl) {
        String baseUrl = serverUrl + (serverUrl.endsWith("/") ? "" : "/");
        return SpotifyHttpManager.makeUri(baseUrl + "api/auth/spotify/redirect");
    }

}
