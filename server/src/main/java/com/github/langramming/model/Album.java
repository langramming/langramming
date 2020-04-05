package com.github.langramming.model;

import lombok.Data;

import java.util.List;

@Data
public class Album {

    private final String id;
    private final String name;
    private final List<Artist> artists;

    public Album(String id, String name, List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.artists = artists;
    }
}
