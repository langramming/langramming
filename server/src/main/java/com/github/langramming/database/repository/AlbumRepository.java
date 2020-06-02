package com.github.langramming.database.repository;

import com.github.langramming.database.model.AlbumEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    default Optional<AlbumEntity> findById(@Nonnull TrackProviderType trackProviderType, @Nonnull String albumId) {
        AlbumEntity exampleEntity = AlbumEntity.builder()
                .provider(trackProviderType.getId())
                .providerAlbumId(albumId)
                .build();

        return findOne(Example.of(exampleEntity));
    }

    default Collection<AlbumEntity> findByIds(@Nonnull TrackProviderType trackProviderType, @Nonnull Collection<String> albumIds) {
        return findAll().stream()
                .filter(trackEntity -> trackProviderType.getId().equals(trackEntity.provider))
                .filter(trackEntity -> albumIds.contains(trackEntity.providerAlbumId))
                .collect(Collectors.toSet());
    }

}
