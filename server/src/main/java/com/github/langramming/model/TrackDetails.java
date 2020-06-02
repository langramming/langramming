package com.github.langramming.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackDetails {
    private final TrackProviderType providerType;
    private final String id;
    private final String name;
    private final List<Album> albums;
    private final List<Artist> artists;

    @Data
    @Builder
    public static class Album {
        private final String id;
        private final String name;
    }

    @Data
    @Builder
    public static class Artist {
        private final String id;
        private final String name;
    }
}
