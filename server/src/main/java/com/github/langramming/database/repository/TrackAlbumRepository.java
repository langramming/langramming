package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackAlbumEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface TrackAlbumRepository extends JpaRepository<TrackAlbumEntity, Long> {

    default Optional<TrackAlbumEntity> getById(@Nonnull TrackProviderType trackProviderType, @Nonnull String albumId) {
        TrackAlbumEntity exampleEntity = new TrackAlbumEntity();
        exampleEntity.provider = trackProviderType.getId();
        exampleEntity.providerAlbumId = albumId;

        return findOne(Example.of(exampleEntity));
    }

}
