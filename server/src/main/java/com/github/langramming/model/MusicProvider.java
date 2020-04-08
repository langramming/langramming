package com.github.langramming.model;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface MusicProvider {

    @NotNull Optional<TrackDetails> getTrackDetails(@NotNull String trackId);

}
