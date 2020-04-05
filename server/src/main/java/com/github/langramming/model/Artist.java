package com.github.langramming.model;

import lombok.Data;

@Data
public class Artist {

    private final String id;
    private final String name;

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
