package com.github.langramming.service;

import com.github.langramming.database.model.AlbumEntity;
import com.github.langramming.database.model.ArtistEntity;
import com.github.langramming.database.model.TrackEntity;
import com.github.langramming.database.model.TrackMetadataEntity;
import com.github.langramming.database.repository.AlbumRepository;
import com.github.langramming.database.repository.ArtistRepository;
import com.github.langramming.database.repository.TrackMetadataRepository;
import com.github.langramming.database.repository.TrackRepository;
import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TrackService {

    private final EnumMap<TrackProviderType, TrackProvider> trackProviderEnumMap;
    private final TrackRepository trackRepository;
    private final TrackMetadataRepository trackMetadataRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    @Inject
    public TrackService(
            TrackRepository trackRepository,
            TrackMetadataRepository trackMetadataRepository,
            AlbumRepository albumRepository,
            ArtistRepository artistRepository,
            List<TrackProvider> trackProviderList) {
        this.trackRepository = trackRepository;
        this.trackMetadataRepository = trackMetadataRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;

        this.trackProviderEnumMap = new EnumMap<>(TrackProviderType.class);
        trackProviderList.forEach(provider ->
                trackProviderEnumMap.put(provider.getType(), provider));
    }

    @Nonnull
    public Optional<TrackDetails> getTrackDetails(@Nonnull TrackProviderType trackProviderType, @Nonnull String trackId) {
        Optional<TrackEntity> trackEntityOpt = trackRepository.findById(trackProviderType, trackId);
        if (trackEntityOpt.isPresent()) {
            List<TrackMetadataEntity> trackMetadataEntities = trackMetadataRepository.findAll(
                    Example.of(
                            TrackMetadataEntity.builder()
                                    .track(TrackEntity.builder()
                                            .id(trackEntityOpt.get().id)
                                            .build())
                                    .build())
            );

            return trackEntityOpt.map(
                    trackEntity -> toTrackDetails(trackProviderType, trackEntity, trackMetadataEntities)
            );
        }

        Optional<TrackDetails> trackDetailsOpt = Optional.ofNullable(trackProviderEnumMap.get(trackProviderType))
                .flatMap(trackProvider -> trackProvider.getTrackDetails(trackId));

        trackDetailsOpt.ifPresent(this::persistTrack);

        return trackDetailsOpt;
    }

    private TrackDetails toTrackDetails(TrackProviderType trackProviderType, TrackEntity trackEntity, List<TrackMetadataEntity> trackMetadataEntities) {
        return TrackDetails.builder()
                .id(trackEntity.providerTrackId)
                .name(trackEntity.name)
                .providerType(trackProviderType)
                .albums(getAlbums(trackMetadataEntities))
                .artists(getArtists(trackMetadataEntities))
                .build();
    }

    private List<TrackDetails.Album> getAlbums(List<TrackMetadataEntity> trackMetadataEntities) {
        List<Long> albumIds = trackMetadataEntities.stream()
                .filter(md -> md.trackMetadataType == TrackMetadataEntity.TrackMetadataType.ALBUM)
                .map(md -> md.value)
                .collect(Collectors.toList());

        return albumRepository.findAllById(albumIds).stream()
                .map(album -> TrackDetails.Album.builder()
                        .id(album.providerAlbumId)
                        .name(album.name)
                        .build())
                .collect(Collectors.toList());
    }

    private List<TrackDetails.Artist> getArtists(List<TrackMetadataEntity> trackMetadataEntities) {
        List<Long> artistIds = trackMetadataEntities.stream()
                .filter(md -> md.trackMetadataType == TrackMetadataEntity.TrackMetadataType.ARTIST)
                .map(md -> md.value)
                .collect(Collectors.toList());

        return artistRepository.findAllById(artistIds).stream()
                .map(artist -> TrackDetails.Artist.builder()
                        .id(artist.providerArtistId)
                        .name(artist.name)
                        .build())
                .collect(Collectors.toList());
    }

    private void persistTrack(TrackDetails trackDetails) {
        TrackProviderType trackProvider = trackDetails.getProviderType();
        TrackEntity trackEntity = trackRepository.save(
                TrackEntity.builder()
                        .provider(trackProvider.getId())
                        .providerTrackId(trackDetails.getId())
                        .name(trackDetails.getName())
                        .build()
        );

        Collection<AlbumEntity> albumEntities = persistAlbums(trackProvider, trackDetails.getAlbums());
        Collection<ArtistEntity> artistEntities = persistArtists(trackProvider, trackDetails.getArtists());

        trackMetadataRepository.saveAll(
                Stream.concat(
                        albumEntities.stream()
                                .map(albumEntity -> TrackMetadataEntity.builder()
                                        .track(trackEntity)
                                        .trackMetadataType(TrackMetadataEntity.TrackMetadataType.ALBUM)
                                        .value(albumEntity.id)
                                        .build()),
                        artistEntities.stream()
                                .map(artistEntity -> TrackMetadataEntity.builder()
                                        .track(trackEntity)
                                        .trackMetadataType(TrackMetadataEntity.TrackMetadataType.ARTIST)
                                        .value(artistEntity.id)
                                        .build())
                ).collect(Collectors.toList())
        );
    }

    private Collection<AlbumEntity> persistAlbums(TrackProviderType trackProvider, List<TrackDetails.Album> albums) {
        Collection<AlbumEntity> existingAlbumEntities = albumRepository.findByIds(
            trackProvider,
            albums.stream().map(TrackDetails.Album::getId).collect(Collectors.toList()));
        Set<String> savedAlbumIds = existingAlbumEntities.stream().map(a -> a.providerAlbumId).collect(Collectors.toSet());

        List<AlbumEntity> newAlbumEntities = albumRepository.saveAll(
                albums.stream()
                        .filter(album -> !savedAlbumIds.contains(album.getId()))
                        .map(album -> AlbumEntity.builder()
                                .provider(trackProvider.getId())
                                .providerAlbumId(album.getId())
                                .name(album.getName())
                                .build())
                        .collect(Collectors.toList())
        );

        return Stream.concat(
                existingAlbumEntities.stream(),
                newAlbumEntities.stream()
        ).collect(Collectors.toSet());
    }

    private Collection<ArtistEntity> persistArtists(TrackProviderType trackProvider, List<TrackDetails.Artist> artists) {
        Collection<ArtistEntity> existingArtistEntities = artistRepository.findByIds(
            trackProvider,
            artists.stream().map(TrackDetails.Artist::getId).collect(Collectors.toList()));
        Set<String> savedArtistIds = existingArtistEntities.stream().map(a -> a.providerArtistId).collect(Collectors.toSet());

        List<ArtistEntity> newAlbumEntities = artistRepository.saveAll(
                artists.stream()
                        .filter(artist -> !savedArtistIds.contains(artist.getId()))
                        .map(artist -> ArtistEntity.builder()
                                .provider(trackProvider.getId())
                                .providerArtistId(artist.getId())
                                .name(artist.getName())
                                .build())
                        .collect(Collectors.toList())
        );

        return Stream.concat(
                existingArtistEntities.stream(),
                newAlbumEntities.stream()
        ).collect(Collectors.toSet());
    }

}
