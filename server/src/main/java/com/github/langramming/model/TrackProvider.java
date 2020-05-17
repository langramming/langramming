package com.github.langramming.model;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface TrackProvider {

    @Nonnull TrackProviderType getType();

    @Nonnull Optional<TrackDetails> getTrackDetails(@NotNull String trackId);

}
