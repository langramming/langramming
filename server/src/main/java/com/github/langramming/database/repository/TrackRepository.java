package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {

    default Optional<TrackEntity> getById(@Nonnull TrackProviderType trackProviderType, @Nonnull String trackId) {
        TrackEntity exampleEntity = new TrackEntity();
        exampleEntity.provider = trackProviderType.getName();
        exampleEntity.providerTrackId = trackId;

        return findOne(Example.of(exampleEntity));
    }

}
