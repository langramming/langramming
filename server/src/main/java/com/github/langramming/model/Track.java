package com.github.langramming.model;

import lombok.Data;

import java.util.List;

@Data
public class Track {

    private final String id;
    private final String name;
    private final Album album;
    private final List<Artist> artists;

    public Track(String id, String name, Album album, List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.artists = artists;
    }
}
