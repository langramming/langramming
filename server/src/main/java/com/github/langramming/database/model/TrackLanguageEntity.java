package com.github.langramming.database.model;

import javax.persistence.*;

@Entity(name = "TaggedTrackLanguage")
@Table(name = "track_language_v1")
public class TrackLanguageEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private TrackEntity track;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private LanguageEntity language;

}
