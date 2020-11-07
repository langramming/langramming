package dev.nickrobson.langramming.client.spotify;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import dev.nickrobson.langramming.model.SpotifyUser;
import dev.nickrobson.langramming.model.TrackDetails;
import dev.nickrobson.langramming.model.TrackProvider;
import dev.nickrobson.langramming.model.TrackProviderType;
import dev.nickrobson.langramming.service.SpotifyUserService;
import java.io.IOException;
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
                .images(toTrackDetailsImages(track.getAlbum().getImages()))
                .links(toTrackDetailsLinks(track))
                .album(toTrackDetailsAlbum(track.getAlbum()))
                .artists(toTrackDetailsArtists(track.getArtists()))
                .build()
        );
    }

    private TrackDetails.Links toTrackDetailsLinks(Track track) {
        return TrackDetails
            .Links.builder()
            .url(track.getExternalUrls().getExternalUrls().get("spotify"))
            .build();
    }

    private List<TrackDetails.Image> toTrackDetailsImages(Image[] images) {
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

    private Optional<TrackDetails.Album> toTrackDetailsAlbum(AlbumSimplified album) {
        return Optional.of(
            TrackDetails.Album.builder().id(album.getId()).name(album.getName()).build()
        );
    }

    private List<TrackDetails.Artist> toTrackDetailsArtists(ArtistSimplified[] artists) {
        return Stream
            .of(artists)
            .map(
                artist ->
                    TrackDetails.Artist.builder().id(artist.getId()).name(artist.getName()).build()
            )
            .collect(Collectors.toList());
    }
}
