package dev.nickrobson.langramming.database.model;

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
@Entity(name = "TrackImage")
@Table(
    name = "track_image_v1",
    uniqueConstraints = @UniqueConstraint(columnNames = { "track_id", "width", "height" })
)
public class TrackImageEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "id")
    public TrackEntity track;

    @Column(name = "width")
    public Integer width;

    @Column(name = "height")
    public Integer height;

    @Column(name = "url")
    public String url;
}
