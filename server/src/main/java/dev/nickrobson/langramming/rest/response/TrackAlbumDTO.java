package dev.nickrobson.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nickrobson.langramming.model.TrackDetails;

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
