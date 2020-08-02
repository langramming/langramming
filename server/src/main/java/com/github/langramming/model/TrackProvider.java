package com.github.langramming.model;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

public interface TrackProvider {
    @Nonnull
    TrackProviderType getType();

    @Nonnull
    Optional<TrackDetails> getTrackDetails(@NotNull String trackId);
}
