package com.github.langramming.rest;

import com.github.langramming.model.TrackProviderType;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.rest.response.TrackAlbumDTO;
import com.github.langramming.rest.response.TrackArtistDTO;
import com.github.langramming.rest.response.TrackDTO;
import com.github.langramming.service.TrackService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/track/details")
public class TrackDetailsResource {

    private final TrackService trackService;

    @Inject
    public TrackDetailsResource(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackDTO> getTrackDetails(
            @RequestParam("provider") @NotNull TrackProviderType trackProviderType,
            @RequestParam("id") @NotNull String trackId
    ) {
        Optional<TrackDetails> trackDetailsOpt = trackService.getTrackDetails(trackProviderType, trackId);

        if (trackDetailsOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(toTrackDTO(trackDetailsOpt.get()));
    }

    private TrackDTO toTrackDTO(TrackDetails trackDetails) {
        return TrackDTO.builder()
                .provider(trackDetails.getProviderType())
                .id(trackDetails.getId())
                .name(trackDetails.getName())
                .album(toTrackAlbumDTO(trackDetails.getAlbum()))
                .artists(trackDetails.getArtists()
                        .stream()
                        .map(this::toTrackArtistDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private TrackAlbumDTO toTrackAlbumDTO(TrackDetails.Album albumDetails) {
        return TrackAlbumDTO.builder()
                .id(albumDetails.getId())
                .name(albumDetails.getName())
                .build();
    }

    private TrackArtistDTO toTrackArtistDTO(TrackDetails.Artist artistDetails) {
        return TrackArtistDTO.builder()
                .id(artistDetails.getId())
                .name(artistDetails.getName())
                .build();
    }

}
