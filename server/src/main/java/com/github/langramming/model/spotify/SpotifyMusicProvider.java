package com.github.langramming.model.spotify;

import com.github.langramming.model.MusicProvider;
import com.github.langramming.model.TrackDetails;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class SpotifyMusicProvider implements MusicProvider {
    @Override
    public @NotNull Optional<TrackDetails> getTrackDetails(@NotNull String trackId) {
        // TODO
        return Optional.empty();
    }
}
