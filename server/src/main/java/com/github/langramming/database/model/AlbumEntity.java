package com.github.langramming.database.model;

import javax.persistence.*;

@Entity(name = "Album")
@Table(name = "album_v1")
public class AlbumEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private TrackProviderEntity provider;

    private String providerAlbumId;

}
