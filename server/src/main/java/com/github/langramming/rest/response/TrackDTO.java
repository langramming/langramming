package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.github.langramming.model.TrackProviderType;
import lombok.Builder;

import java.util.List;

@Builder
@JsonAutoDetect
public class TrackDTO {

    public TrackProviderType provider;
    public String id;
    public String name;
    public TrackAlbumDTO album;
    public List<TrackArtistDTO> artists;

}
