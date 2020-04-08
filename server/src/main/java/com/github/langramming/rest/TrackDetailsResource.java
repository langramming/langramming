package com.github.langramming.rest;

import com.github.langramming.model.MusicProviderType;
import com.github.langramming.model.TrackDetails;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@Path("/track/details")
public class TrackDetailsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TrackDetails getTrackDetails(
            @QueryParam("provider") @NotNull MusicProviderType provider,
            @QueryParam("id") @NotNull String id
    ) {
        TrackDetails.Artist artist = new TrackDetails.Artist("artist id", "artist name");
        TrackDetails.Album album = new TrackDetails.Album("album id", "album name", Collections.singletonList(artist));
        return new TrackDetails(provider, "track id", "track name", album, Collections.singletonList(artist));
    }

}
