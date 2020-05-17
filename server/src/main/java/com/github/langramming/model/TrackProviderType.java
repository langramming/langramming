package com.github.langramming.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TrackProviderType {

    SPOTIFY("spotify", "Spotify");

    @JsonValue
    private final String id;
    private final String name;

    TrackProviderType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private static final Map<String, TrackProviderType> musicProviderTypeCache = new HashMap<>();

    static {
        for (TrackProviderType trackProviderType : TrackProviderType.values()) {
            musicProviderTypeCache.put(trackProviderType.getId().toLowerCase(), trackProviderType);
        }
    }

    @JsonCreator
    public static TrackProviderType fromString(String providerType) {
        return providerType == null ? null : musicProviderTypeCache.get(providerType.toLowerCase());
    }

}
