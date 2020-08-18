package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.TrackDetails;

@JsonAutoDetect
public class TrackImageDTO {
    @JsonProperty("width")
    public final int width;

    @JsonProperty("height")
    public final int height;

    @JsonProperty("url")
    public final String url;

    public TrackImageDTO(TrackDetails.Image image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.url = image.getUrl();
    }
}
