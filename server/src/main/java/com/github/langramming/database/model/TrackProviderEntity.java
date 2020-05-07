package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "TrackProvider")
@Table(name = "track_provider_v1")
public class TrackProviderEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
