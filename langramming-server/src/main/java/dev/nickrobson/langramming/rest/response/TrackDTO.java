package dev.nickrobson.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nickrobson.langramming.model.TrackDetails;
import dev.nickrobson.langramming.model.TrackProviderType;
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

    @JsonProperty("url")
    public final String url;

    @JsonProperty("album")
    public final Optional<TrackAlbumDTO> album;

    @JsonProperty("artists")
    public final List<TrackArtistDTO> artists;

    @JsonProperty("images")
    public final List<TrackImageDTO> images;

    public TrackDTO(TrackDetails trackDetails) {
        this.provider = trackDetails.getProviderType();
        this.id = trackDetails.getId();
        this.name = trackDetails.getName();
        this.url = trackDetails.getLinks().getUrl();
        this.album = trackDetails.getAlbum().map(TrackAlbumDTO::new);
        this.artists =
            trackDetails
                .getArtists()
                .stream()
                .map(TrackArtistDTO::new)
                .collect(Collectors.toList());
        this.images =
            trackDetails.getImages().stream().map(TrackImageDTO::new).collect(Collectors.toList());
    }
}
