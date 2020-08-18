package com.github.langramming.database.repository;

import com.github.langramming.database.model.AlbumEntity;
import com.github.langramming.model.TrackProviderType;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    Optional<AlbumEntity> findByProviderAndProviderAlbumId(
        @Nonnull String trackProviderId,
        @Nonnull String albumId
    );

    Collection<AlbumEntity> findByProviderAndProviderAlbumIdIn(
        @Nonnull String trackProviderId,
        @Nonnull Collection<String> albumIds
    );

    default Optional<AlbumEntity> findById(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull String albumId
    ) {
        return findByProviderAndProviderAlbumId(
            trackProviderType.getId(),
            albumId
        );
    }

    default Collection<AlbumEntity> findByIds(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull Collection<String> albumIds
    ) {
        return findByProviderAndProviderAlbumIdIn(
            trackProviderType.getId(),
            albumIds
        );
    }
}
