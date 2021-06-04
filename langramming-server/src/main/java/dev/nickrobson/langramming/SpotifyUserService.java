package dev.nickrobson.langramming;

import dev.nickrobson.langramming.client.spotify.SpotifyRestClient;
import dev.nickrobson.langramming.manager.SpotifyUserManager;
import dev.nickrobson.langramming.model.SpotifyUser;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpotifyUserService {

    private final SpotifyUserManager spotifyUserManager;
    private final SpotifyRestClient spotifyRestClient;

    @Inject
    public SpotifyUserService(
        SpotifyUserManager spotifyUserManager,
        SpotifyRestClient spotifyRestClient
    ) {
        this.spotifyUserManager = spotifyUserManager;
        this.spotifyRestClient = spotifyRestClient;
    }

    public Optional<SpotifyUser> getCurrentSpotifyUser() {
        return spotifyUserManager
            .getCurrentSpotifyUser()
            .filter(spotifyRestClient::isAuthenticated);
    }
}
