package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity(name = "TaggedTrackLanguage")
@Table(
    name = "track_language_v1",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = { "track_id", "language_id", "tagged_by_id" }
        ),
    }
)
public class TrackLanguageEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private TrackEntity track;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private LanguageEntity language;

    @ManyToOne
    @JoinColumn(name = "tagged_by_id", nullable = false)
    private UserEntity tagged_by;
}
