package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TrackMetadata")
@Table(
    name = "track_metadata_v1",
    uniqueConstraints = { @UniqueConstraint(columnNames = "track_id") }
)
public class TrackMetadataEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    public TrackEntity track;

    @Column(name = "metadata_type")
    @Enumerated(EnumType.STRING)
    public TrackMetadataType trackMetadataType;

    @Column(name = "value")
    public Long value;

    public enum TrackMetadataType {
        ALBUM,
        ARTIST,
    }
}
