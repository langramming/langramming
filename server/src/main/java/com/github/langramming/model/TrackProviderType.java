package com.github.langramming.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TrackProviderType {

    SPOTIFY("spotify");

    @JsonValue
    private final String id;

    TrackProviderType(String id) {
        this.id = id;
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
