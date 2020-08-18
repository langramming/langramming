package com.github.langramming.database.model;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ParametersAreNonnullByDefault
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
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "provider", nullable = false)
    public String provider;

    @Column(name = "provider_track_id", nullable = false)
    public String providerTrackId;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "album", referencedColumnName = "id", updatable = false)
    public AlbumEntity album;

    @ManyToMany
    @JoinTable(
        name = "track_artists_v1",
        joinColumns = @JoinColumn(
            name = "track_id",
            referencedColumnName = "id"
        ),
        inverseJoinColumns = @JoinColumn(
            name = "artist_id",
            referencedColumnName = "id"
        )
    )
    public List<ArtistEntity> artists;

    @OneToMany(mappedBy = "track")
    public List<TrackImageEntity> trackImages;

    @OneToMany(mappedBy = "track")
    public List<TrackLanguageEntity> trackLanguages;
}
