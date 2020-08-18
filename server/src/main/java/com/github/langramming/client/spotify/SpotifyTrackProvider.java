package com.github.langramming.client.spotify;

import com.github.langramming.model.SpotifyUser;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;
import com.github.langramming.service.SpotifyUserService;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class SpotifyTrackProvider implements TrackProvider {
    private final SpotifyRestClient spotifyRestClient;
    private final SpotifyUserService spotifyUserService;

    @Inject
    public SpotifyTrackProvider(
        SpotifyRestClient spotifyRestClient,
        SpotifyUserService spotifyUserService
    ) {
        this.spotifyRestClient = spotifyRestClient;
        this.spotifyUserService = spotifyUserService;
    }

    @Nonnull
    @Override
    public TrackProviderType getType() {
        return TrackProviderType.SPOTIFY;
    }

    @Nonnull
    @Override
    public Optional<TrackDetails> getTrackDetails(@NotNull String trackId) {
        Optional<SpotifyUser> spotifyUserOpt = spotifyUserService.getCurrentSpotifyUser();
        if (spotifyUserOpt.isEmpty()) {
            return Optional.empty();
        }

        GetTrackRequest getTrackRequest = spotifyRestClient
            .getAuthenticatedSpotifyApi()
            .getTrack(trackId)
            .build();

        Track track;
        try {
            track = getTrackRequest.execute();
        } catch (SpotifyWebApiException | IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(
            TrackDetails
                .builder()
                .providerType(TrackProviderType.SPOTIFY)
                .id(track.getId())
                .name(track.getName())
                .images(toTrackDetailsImage(track.getAlbum().getImages()))
                .urls(toTrackDetailsUrls(track))
                .album(toTrackDetailsAlbum(track.getAlbum()))
                .artists(toTrackDetailsArtists(track.getArtists()))
                .build()
        );
    }

    private TrackDetails.Urls toTrackDetailsUrls(Track track) {
        return TrackDetails
            .Urls.builder()
            .preview(track.getPreviewUrl())
            .url(track.getExternalUrls().getExternalUrls().get("spotify"))
            .build();
    }

    private List<TrackDetails.Image> toTrackDetailsImage(Image[] images) {
        return Stream
            .of(images)
            .map(
                image ->
                    TrackDetails
                        .Image.builder()
                        .height(image.getHeight())
                        .width(image.getWidth())
                        .url(image.getUrl())
                        .build()
            )
            .collect(Collectors.toList());
    }

    private Optional<TrackDetails.Album> toTrackDetailsAlbum(
        AlbumSimplified album
    ) {
        return Optional.of(
            TrackDetails
                .Album.builder()
                .id(album.getId())
                .name(album.getName())
                .build()
        );
    }

    private List<TrackDetails.Artist> toTrackDetailsArtists(
        ArtistSimplified[] artists
    ) {
        return Stream
            .of(artists)
            .map(
                artist ->
                    TrackDetails
                        .Artist.builder()
                        .id(artist.getId())
                        .name(artist.getName())
                        .build()
            )
            .collect(Collectors.toList());
    }
}
