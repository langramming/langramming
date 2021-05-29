package dev.nickrobson.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nickrobson.langramming.model.TrackDetails;
import java.util.Date;

@JsonAutoDetect
public class RecentSpotifyTrackDTO {

    @JsonProperty("track")
    public final TrackDTO track;

    @JsonProperty("playedAt")
    public final Date playedAt;

    @JsonProperty("previewUrl")
    public final String previewUrl;

    public RecentSpotifyTrackDTO(TrackDetails trackDetails, Date playedAt, String previewUrl) {
        this.track = new TrackDTO(trackDetails);
        this.playedAt = playedAt;
        this.previewUrl = previewUrl;
    }
}
