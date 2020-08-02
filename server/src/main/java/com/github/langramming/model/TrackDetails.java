package com.github.langramming.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

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
