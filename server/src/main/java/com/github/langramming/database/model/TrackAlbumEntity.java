package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "TrackAlbum")
@Table(name = "track_album_v1")
public class TrackAlbumEntity {

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    public String provider;

    public String providerAlbumId;

}
