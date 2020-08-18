package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;

@JsonAutoDetect
public class TrackArtistDTO {
    @JsonProperty("id")
    public final String id;

    @JsonProperty("name")
    public final String name;

    public TrackArtistDTO(TrackDetails.Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
    }
}
