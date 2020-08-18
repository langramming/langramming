package com.github.langramming.service;

import com.github.langramming.database.model.AlbumEntity;
import com.github.langramming.database.model.ArtistEntity;
import com.github.langramming.database.model.TrackEntity;
import com.github.langramming.database.model.TrackImageEntity;
import com.github.langramming.database.repository.AlbumRepository;
import com.github.langramming.database.repository.ArtistRepository;
import com.github.langramming.database.repository.TrackImageRepository;
import com.github.langramming.database.repository.TrackRepository;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;
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
public class TrackService {
    private final EnumMap<TrackProviderType, TrackProvider> trackProviderEnumMap;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TrackImageRepository trackImageRepository;

    @Inject
    public TrackService(
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
        Optional<TrackEntity> trackEntityOpt = trackRepository.findById(
            trackProviderType,
            trackId
        );
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
            .album(
                Optional
                    .ofNullable(trackEntity.album)
                    .map(
                        album ->
                            TrackDetails
                                .Album.builder()
                                .id(album.providerAlbumId)
                                .name(album.name)
                                .build()
                    )
            )
            .artists(
                trackEntity
                    .artists.stream()
                    .map(
                        artist ->
                            TrackDetails
                                .Artist.builder()
                                .id(artist.providerArtistId)
                                .name(artist.name)
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
        List<ArtistEntity> artists = persistArtists(
            trackProvider,
            trackDetails.getArtists()
        );

        TrackEntity trackEntity = trackRepository.save(
            TrackEntity
                .builder()
                .provider(trackProvider.getId())
                .providerTrackId(trackDetails.getId())
                .name(trackDetails.getName())
                .album(albumOpt.orElse(null))
                .artists(artists)
                .build()
        );

        persistImages(trackEntity, trackDetails.getImages());
    }

    private AlbumEntity persistAlbum(
        TrackProviderType trackProvider,
        TrackDetails.Album album
    ) {
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
            artists
                .stream()
                .map(TrackDetails.Artist::getId)
                .collect(Collectors.toList())
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

    private void persistImages(
        TrackEntity trackEntity,
        List<TrackDetails.Image> images
    ) {
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
