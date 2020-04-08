package com.github.langramming.model;

import lombok.Data;

import java.util.List;

@Data
public class TrackDetails {
    private final MusicProviderType providerType;
    private final String id;
    private final String name;
    private final Album album;
    private final List<Artist> artists;

    @Data
    public static class Album {
        private final String id;
        private final String name;
        private final List<Artist> artists;
    }

    @Data
    public static class Artist {
        private final String id;
        private final String name;
    }
}
