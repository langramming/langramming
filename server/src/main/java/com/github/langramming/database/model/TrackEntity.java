package com.github.langramming.database.model;

import javax.persistence.*;

@Entity(name = "Track")
@Table(name = "track_v1")
public class TrackEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private TrackProviderEntity provider;

    private String providerTrackId;

}
