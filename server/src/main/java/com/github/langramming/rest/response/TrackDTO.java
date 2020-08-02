package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProviderType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;

@JsonAutoDetect
public class TrackDTO {
    @JsonProperty("provider")
    public final TrackProviderType provider;

    @JsonProperty("id")
    public final String id;

    @JsonProperty("name")
    public final String name;

    @JsonProperty("album")
    public final List<TrackAlbumDTO> album;

    @JsonProperty("artist")
    public final List<TrackArtistDTO> artists;

    public TrackDTO(TrackDetails trackDetails) {
        this.provider = trackDetails.getProviderType();
        this.id = trackDetails.getId();
        this.name = trackDetails.getName();
        this.album =
            trackDetails
                .getAlbums()
                .stream()
                .map(TrackAlbumDTO::new)
                .collect(Collectors.toList());
        this.artists =
            trackDetails
                .getArtists()
                .stream()
                .map(TrackArtistDTO::new)
                .collect(Collectors.toList());
    }
}
