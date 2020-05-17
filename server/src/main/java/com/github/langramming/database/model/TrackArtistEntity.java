package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "TrackArtist")
@Table(name = "track_artist_v1", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "providerArtistId"})
})
public class TrackArtistEntity {

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    public String provider;

    public String providerArtistId;

}
