package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {

    default Optional<TrackEntity> findById(@Nonnull TrackProviderType trackProviderType, @Nonnull String trackId) {
        TrackEntity exampleEntity = new TrackEntity();
        exampleEntity.provider = trackProviderType.getId();
        exampleEntity.providerTrackId = trackId;

        return findOne(Example.of(exampleEntity));
    }

    default Collection<TrackEntity> findByIds(@Nonnull TrackProviderType trackProviderType, @Nonnull Collection<String> trackIds) {
        return findAll().stream()
                .filter(trackEntity -> trackProviderType.getId().equals(trackEntity.provider))
                .filter(trackEntity -> trackIds.contains(trackEntity.providerTrackId))
                .collect(Collectors.toSet());
    }

}
