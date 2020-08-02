package com.github.langramming.service;

import com.github.langramming.database.model.SpotifyUserEntity;
import com.github.langramming.database.model.UserEntity;
import com.github.langramming.database.repository.SpotifyUserRepository;
import com.github.langramming.model.SpotifyUser;
import com.github.langramming.model.User;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpotifyUserService {
    private final UserService.UserProvider userProvider;
    private final SpotifyUserRepository spotifyUserRepository;

    @Inject
    private SpotifyUserService(
        UserService.UserProvider userProvider,
        SpotifyUserRepository spotifyUserRepository
    ) {
        this.userProvider = userProvider;
        this.spotifyUserRepository = spotifyUserRepository;
    }

    public Optional<SpotifyUser> getCurrentSpotifyUser() {
        Optional<User> userOpt = userProvider.get();
        return userOpt
            .flatMap(user -> spotifyUserRepository.findByUserId(user.getId()))
            .map(this::toSpotifyUser);
    }

    public SpotifyUser createOrUpdateUser(
        AuthorizationCodeCredentials credentials
    ) {
        Optional<User> userOpt = userProvider.get();
        if (userOpt.isEmpty()) {
            throw new IllegalStateException("no logged in user");
        }
        User user = userOpt.get();

        Optional<SpotifyUserEntity> existingUserEntity = spotifyUserRepository.findByUserId(
            user.getId()
        );

        SpotifyUserEntity spotifyUserEntity = SpotifyUserEntity
            .builder()
            .id(existingUserEntity.map(u -> u.id).orElse(null))
            .tokenType(credentials.getTokenType())
            .scope(credentials.getScope())
            .expiresAt(
                Instant
                    .now()
                    .plus(credentials.getExpiresIn(), ChronoUnit.SECONDS)
                    .getEpochSecond()
            )
            .accessToken(credentials.getAccessToken())
            .refreshToken(credentials.getRefreshToken())
            .user(UserEntity.builder().id(user.getId()).build())
            .build();

        return toSpotifyUser(spotifyUserRepository.save(spotifyUserEntity));
    }

    private SpotifyUser toSpotifyUser(SpotifyUserEntity spotifyUserEntity) {
        return SpotifyUser
            .builder()
            .id(spotifyUserEntity.id)
            .userId(spotifyUserEntity.user.id)
            .accessToken(spotifyUserEntity.accessToken)
            .refreshToken(spotifyUserEntity.refreshToken)
            .expiresAt(spotifyUserEntity.expiresAt)
            .build();
    }
}
