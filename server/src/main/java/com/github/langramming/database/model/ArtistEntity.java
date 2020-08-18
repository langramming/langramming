package com.github.langramming.database.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Artist")
@Table(
    name = "artist_v1",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "provider", "provider_artist_id" }),
    }
)
public class ArtistEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "provider", nullable = false)
    public String provider;

    @Column(name = "provider_artist_id", nullable = false)
    public String providerArtistId;

    @ManyToMany
    @JoinTable(
        name = "track_artists_v1",
        joinColumns = @JoinColumn(
            name = "artist_id",
            referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "track_id",
            referencedColumnName = "id"
        )
    )
    public List<TrackEntity> tracks;
}
