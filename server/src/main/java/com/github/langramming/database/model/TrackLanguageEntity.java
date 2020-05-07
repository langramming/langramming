package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "tagged_by_id")
    private TelegramUserEntity tagged_by;

}
