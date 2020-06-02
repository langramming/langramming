package com.github.langramming.service;

import com.github.langramming.database.model.SpotifyUserEntity;
import com.github.langramming.database.model.TelegramUserEntity;
import com.github.langramming.database.repository.SpotifyUserRepository;
import com.github.langramming.model.SpotifyUser;
import com.github.langramming.model.User;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import org.springframework.data.domain.Example;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Singleton
public class SpotifyUserService {

    private final UserService.UserProvider userProvider;
    private final SpotifyUserRepository spotifyUserRepository;

    @Inject
    private SpotifyUserService(
            UserService.UserProvider userProvider,
            SpotifyUserRepository spotifyUserRepository) {
        this.userProvider = userProvider;
        this.spotifyUserRepository = spotifyUserRepository;
    }

    public Optional<SpotifyUser> getCurrentSpotifyUser() {
        Optional<User> userOpt = userProvider.get();
        return userOpt
                .flatMap(user -> findCurrentSpotifyUser(user.getId()))
                .map(this::toSpotifyUser);
    }

    public SpotifyUser createOrUpdateUser(AuthorizationCodeCredentials credentials) {
        Optional<User> userOpt = userProvider.get();
        if (userOpt.isEmpty()) {
            throw new IllegalStateException("no logged in user");
        }

        Optional<SpotifyUserEntity> existingUserEntity = findCurrentSpotifyUser(userOpt.get().getId());

        SpotifyUserEntity spotifyUserEntity = SpotifyUserEntity.builder()
                .id(existingUserEntity.map(ent -> ent.id).orElse(null))
                .token_type(credentials.getTokenType())
                .scope(credentials.getScope())
                .expires_at(Instant.now()
                        .plus(credentials.getExpiresIn(), ChronoUnit.SECONDS)
                        .getEpochSecond())
                .access_token(credentials.getAccessToken())
                .refresh_token(credentials.getRefreshToken())
                .telegram_user(TelegramUserEntity.builder()
                        .id(userOpt.get().getId())
                        .build())
                .build();

        return toSpotifyUser(spotifyUserRepository.save(spotifyUserEntity));
    }

    private Optional<SpotifyUserEntity> findCurrentSpotifyUser(long userId) {
        return spotifyUserRepository.findOne(
                Example.of(
                        SpotifyUserEntity.builder()
                                .telegram_user(TelegramUserEntity.builder()
                                        .id(userId)
                                        .build())
                                .build())
        );
    }

    private SpotifyUser toSpotifyUser(SpotifyUserEntity spotifyUserEntity) {
        return SpotifyUser.builder()
                .id(spotifyUserEntity.id)
                .userId(spotifyUserEntity.telegram_user.id)
                .accessToken(spotifyUserEntity.access_token)
                .refreshToken(spotifyUserEntity.refresh_token)
                .expiresAt(spotifyUserEntity.expires_at)
                .build();
    }
}
