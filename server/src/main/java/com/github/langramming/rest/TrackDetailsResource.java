package com.github.langramming.rest;

import com.github.langramming.model.TrackProviderType;
import com.github.langramming.model.TrackDetails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collections;

@RestController
@RequestMapping("/api/track/details")
public class TrackDetailsResource {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TrackDetails getTrackDetails(
            @RequestParam("provider") @NotNull TrackProviderType provider,
            @RequestParam("id") @NotNull String id
    ) {
        TrackDetails.Artist artist = new TrackDetails.Artist("artist id", "artist name");
        TrackDetails.Album album = new TrackDetails.Album("album id", "album name", Collections.singletonList(artist));
        return new TrackDetails(provider, "track id", "track name", album, Collections.singletonList(artist));
    }

}
