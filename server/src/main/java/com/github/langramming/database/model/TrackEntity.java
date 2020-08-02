package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Track")
@Table(
    name = "track_v1",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "provider", "provider_track_id" }),
    }
)
public class TrackEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "provider")
    public String provider;

    @Column(name = "provider_track_id")
    public String providerTrackId;
}
