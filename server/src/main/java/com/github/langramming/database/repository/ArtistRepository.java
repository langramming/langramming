package com.github.langramming.database.repository;

import com.github.langramming.database.model.ArtistEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {

    default Optional<ArtistEntity> findById(@Nonnull TrackProviderType trackProviderType, @Nonnull String artistId) {
        ArtistEntity exampleEntity = ArtistEntity.builder()
                .provider(trackProviderType.getId())
                .providerArtistId(artistId)
                .build();

        return findOne(Example.of(exampleEntity));
    }

    default Collection<ArtistEntity> findByIds(@Nonnull TrackProviderType trackProviderType, @Nonnull Collection<String> artistIds) {
        return findAll().stream()
                .filter(trackEntity -> trackProviderType.getId().equals(trackEntity.provider))
                .filter(trackEntity -> artistIds.contains(trackEntity.providerArtistId))
                .collect(Collectors.toSet());
    }

}
