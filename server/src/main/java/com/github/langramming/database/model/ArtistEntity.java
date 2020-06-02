package com.github.langramming.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Artist")
@Table(name = "artist_v1", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "provider_artist_id"})
})
public class ArtistEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "provider")
    public String provider;

    @Column(name = "provider_artist_id")
    public String providerArtistId;

}
