package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;
import java.util.Date;

@JsonAutoDetect
public class RecentSpotifyTrackDTO {
    @JsonProperty("track")
    public final TrackDTO track;

    @JsonProperty("playedAt")
    public final Date playedAt;

    public RecentSpotifyTrackDTO(TrackDetails trackDetails, Date playedAt) {
        this.track = new TrackDTO(trackDetails);
        this.playedAt = playedAt;
    }
}
