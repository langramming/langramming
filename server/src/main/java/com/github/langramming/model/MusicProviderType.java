package com.github.langramming.model;

import com.fasterxml.jackson.annotation.*;
import com.github.langramming.model.spotify.SpotifyMusicProvider;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum MusicProviderType {

    SPOTIFY("spotify", SpotifyMusicProvider.class);

    @JsonValue
    private final String id;
    private final MusicProvider musicProvider;

    MusicProviderType(String id, Class<? extends MusicProvider> musicProvider) {
        this.id = id;

        try {
            Constructor<? extends MusicProvider> constructor = musicProvider.getConstructor();
            constructor.setAccessible(true);
            this.musicProvider = constructor.newInstance();
        } catch (ReflectiveOperationException ignored) {
            throw new IllegalStateException("Invalid music provider " + musicProvider.getName() + " - no no-args constructor.");
        }

    }

    private static final Map<String, MusicProviderType> musicProviderTypeCache = new HashMap<>();

    static {
        for (MusicProviderType musicProviderType : MusicProviderType.values()) {
            musicProviderTypeCache.put(musicProviderType.getId().toLowerCase(), musicProviderType);
        }
    }

    @JsonCreator
    public static MusicProviderType fromString(String providerType) {
        return providerType == null ? null : musicProviderTypeCache.get(providerType.toLowerCase());
    }

}
