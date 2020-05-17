package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackArtistEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface TrackArtistRepository extends JpaRepository<TrackArtistEntity, Long> {

    default Optional<TrackArtistEntity> getById(@Nonnull TrackProviderType trackProviderType, @Nonnull String artistId) {
        TrackArtistEntity exampleEntity = new TrackArtistEntity();
        exampleEntity.provider = trackProviderType.getName();
        exampleEntity.providerArtistId = artistId;

        return findOne(Example.of(exampleEntity));
    }

}
