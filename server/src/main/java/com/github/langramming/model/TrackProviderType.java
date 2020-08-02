package com.github.langramming.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum TrackProviderType {
    SPOTIFY("spotify", "Spotify");

    private final String id;
    private final String name;

    TrackProviderType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonValue
    public String getId() {
        return id;
    }

    private static final Map<String, TrackProviderType> trackProviderTypeCache = new HashMap<>();

    static {
        for (TrackProviderType trackProviderType : TrackProviderType.values()) {
            trackProviderTypeCache.put(
                trackProviderType.getId().toLowerCase(),
                trackProviderType
            );
        }
    }
}
