package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProviderType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonAutoDetect
public class TrackDTO {
    @JsonProperty("provider")
    public final TrackProviderType provider;

    @JsonProperty("id")
    public final String id;

    @JsonProperty("name")
    public final String name;

    @JsonProperty("album")
    public final Optional<TrackAlbumDTO> album;

    @JsonProperty("artists")
    public final List<TrackArtistDTO> artists;

    public TrackDTO(TrackDetails trackDetails) {
        this.provider = trackDetails.getProviderType();
        this.id = trackDetails.getId();
        this.name = trackDetails.getName();
        this.album = trackDetails.getAlbum().map(TrackAlbumDTO::new);
        this.artists =
            trackDetails
                .getArtists()
                .stream()
                .map(TrackArtistDTO::new)
                .collect(Collectors.toList());
    }
}
