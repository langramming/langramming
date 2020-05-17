package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "Track")
@Table(name = "track_v1")
public class TrackEntity {

    @Id
    @GeneratedValue
    public Long id;

    public String name;

    public String provider;

    public String providerTrackId;

}
