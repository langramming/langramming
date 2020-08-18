package com.github.langramming.model;

import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackDetails {
    private final TrackProviderType providerType;
    private final String id;
    private final String name;
    private final Links links;
    private final List<Image> images;
    private final Optional<Album> album;
    private final List<Artist> artists;

    @Data
    @Builder
    public static class Album {
        private final String id;
        private final String name;
        private final Optional<String> url;
        private final Optional<String> imageUrl;
    }

    @Data
    @Builder
    public static class Artist {
        private final String id;
        private final String name;
    }

    @Data
    @Builder
    public static class Links {
        private final String url;
    }

    @Data
    @Builder
    public static class Image {
        private final int height;
        private final int width;
        private final String url;
    }
}
