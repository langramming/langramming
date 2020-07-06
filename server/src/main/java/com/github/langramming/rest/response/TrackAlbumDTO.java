package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;

@JsonAutoDetect
public class TrackAlbumDTO {
    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    public TrackAlbumDTO(TrackDetails.Album album) {
        this.id = album.getId();
        this.name = album.getName();
    }
}
