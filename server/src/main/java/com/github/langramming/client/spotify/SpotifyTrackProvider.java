package com.github.langramming.client.spotify;

import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class SpotifyTrackProvider implements TrackProvider {
    @Nonnull
    @Override
    public TrackProviderType getType() {
        return TrackProviderType.SPOTIFY;
    }

    @Nonnull
    @Override
    public Optional<TrackDetails> getTrackDetails(@NotNull String trackId) {
        // TODO
        return Optional.empty();
    }
}
