package dev.nickrobson.langramming.manager;

import dev.nickrobson.langramming.database.model.AlbumEntity;
import dev.nickrobson.langramming.database.model.ArtistEntity;
import dev.nickrobson.langramming.database.model.TrackEntity;
import dev.nickrobson.langramming.database.model.TrackImageEntity;
import dev.nickrobson.langramming.database.repository.AlbumRepository;
import dev.nickrobson.langramming.database.repository.ArtistRepository;
import dev.nickrobson.langramming.database.repository.TrackImageRepository;
import dev.nickrobson.langramming.database.repository.TrackRepository;
import dev.nickrobson.langramming.model.TrackDetails;
import dev.nickrobson.langramming.model.TrackProvider;
import dev.nickrobson.langramming.model.TrackProviderType;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.springframework.transaction.annotation.Transactional;

@Singleton
public class TrackManager {

    private final EnumMap<TrackProviderType, TrackProvider> trackProviderEnumMap;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TrackImageRepository trackImageRepository;

    @Inject
    public TrackManager(
        TrackRepository trackRepository,
        AlbumRepository albumRepository,
        ArtistRepository artistRepository,
        TrackImageRepository trackImageRepository,
        List<TrackProvider> trackProviderList
    ) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.trackImageRepository = trackImageRepository;

        this.trackProviderEnumMap = new EnumMap<>(TrackProviderType.class);
        trackProviderList.forEach(
            provider -> trackProviderEnumMap.put(provider.getType(), provider)
        );
    }

    @Nonnull
    public Optional<TrackDetails> getTrackDetails(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull String trackId
    ) {
        Optional<TrackEntity> trackEntityOpt = trackRepository.findById(trackProviderType, trackId);
        if (trackEntityOpt.isPresent()) {
            return trackEntityOpt.map(
                trackEntity -> toTrackDetails(trackProviderType, trackEntity)
            );
        }

        Optional<TrackDetails> trackDetailsOpt = Optional
            .ofNullable(trackProviderEnumMap.get(trackProviderType))
            .flatMap(trackProvider -> trackProvider.getTrackDetails(trackId));

        trackDetailsOpt.ifPresent(this::persistTrack);

        return trackDetailsOpt;
    }

    private TrackDetails toTrackDetails(
        TrackProviderType trackProviderType,
        TrackEntity trackEntity
    ) {
        return TrackDetails
            .builder()
            .id(trackEntity.providerTrackId)
            .name(trackEntity.name)
            .providerType(trackProviderType)
            .links(TrackDetails.Links.builder().url(trackEntity.url).build())
            .album(
                Optional
                    .ofNullable(trackEntity.album)
                    .map(
                        album ->
                            TrackDetails.Album
                                .builder()
                                .id(album.providerAlbumId)
                                .name(album.name)
                                .build()
                    )
            )
            .artists(
                trackEntity.artists
                    .stream()
                    .map(
                        artist ->
                            TrackDetails.Artist
                                .builder()
                                .id(artist.providerArtistId)
                                .name(artist.name)
                                .build()
                    )
                    .collect(Collectors.toList())
            )
            .images(
                trackEntity.images
                    .stream()
                    .map(
                        image ->
                            TrackDetails.Image
                                .builder()
                                .width(image.width)
                                .height(image.height)
                                .url(image.url)
                                .build()
                    )
                    .collect(Collectors.toList())
            )
            .build();
    }

    @Transactional
    public void persistTrack(TrackDetails trackDetails) {
        TrackProviderType trackProvider = trackDetails.getProviderType();

        Optional<AlbumEntity> albumOpt = trackDetails
            .getAlbum()
            .map(album -> persistAlbum(trackProvider, album));
        List<ArtistEntity> artists = persistArtists(trackProvider, trackDetails.getArtists());

        TrackEntity trackEntity = trackRepository.save(
            TrackEntity
                .builder()
                .provider(trackProvider.getId())
                .providerTrackId(trackDetails.getId())
                .name(trackDetails.getName())
                .url(trackDetails.getLinks().getUrl())
                .album(albumOpt.orElse(null))
                .artists(artists)
                .build()
        );

        persistImages(trackEntity, trackDetails.getImages());
    }

    private AlbumEntity persistAlbum(TrackProviderType trackProvider, TrackDetails.Album album) {
        Optional<AlbumEntity> existingAlbum = albumRepository.findById(
            trackProvider,
            album.getId()
        );

        return existingAlbum.orElseGet(
            () ->
                albumRepository.save(
                    AlbumEntity
                        .builder()
                        .provider(trackProvider.getId())
                        .providerAlbumId(album.getId())
                        .name(album.getName())
                        .build()
                )
        );
    }

    private List<ArtistEntity> persistArtists(
        TrackProviderType trackProvider,
        List<TrackDetails.Artist> artists
    ) {
        Collection<ArtistEntity> existingArtists = artistRepository.findByIds(
            trackProvider,
            artists.stream().map(TrackDetails.Artist::getId).collect(Collectors.toList())
        );

        Set<String> existingArtistIds = existingArtists
            .stream()
            .map(artist -> artist.providerArtistId)
            .collect(Collectors.toSet());

        List<ArtistEntity> newArtists = artistRepository.saveAll(
            artists
                .stream()
                .filter(artist -> !existingArtistIds.contains(artist.getId()))
                .map(
                    artist ->
                        ArtistEntity
                            .builder()
                            .provider(trackProvider.getId())
                            .providerArtistId(artist.getId())
                            .name(artist.getName())
                            .build()
                )
                .collect(Collectors.toList())
        );

        return Stream
            .concat(existingArtists.stream(), newArtists.stream())
            .collect(Collectors.toList());
    }

    private void persistImages(TrackEntity trackEntity, List<TrackDetails.Image> images) {
        Collection<TrackImageEntity> existingImages = trackImageRepository.findAllByTrack(
            trackEntity
        );

        trackImageRepository.saveAll(
            images
                .stream()
                .filter(
                    image -> {
                        for (TrackImageEntity existing : existingImages) {
                            return (
                                existing.width != image.getWidth() ||
                                existing.height != image.getHeight()
                            );
                        }
                        return true;
                    }
                )
                .map(
                    image ->
                        TrackImageEntity
                            .builder()
                            .track(trackEntity)
                            .width(image.getWidth())
                            .height(image.getHeight())
                            .url(image.getUrl())
                            .build()
                )
                .collect(Collectors.toList())
        );
    }
}
