package com.github.langramming.database.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Album")
@Table(
    name = "album_v1",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "provider", "provider_album_id" }) }
)
public class AlbumEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "provider", nullable = false)
    public String provider;

    @Column(name = "provider_album_id", nullable = false)
    public String providerAlbumId;

    @OneToMany(mappedBy = "album")
    public List<TrackEntity> tracks;
}
