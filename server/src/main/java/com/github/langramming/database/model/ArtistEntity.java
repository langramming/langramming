package com.github.langramming.database.model;

import javax.persistence.*;

@Entity(name = "Artist")
@Table(name = "artist_v1")
public class ArtistEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private TrackProviderEntity provider;

    private String providerArtistId;

}
