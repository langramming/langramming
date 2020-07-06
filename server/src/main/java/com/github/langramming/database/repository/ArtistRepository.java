package com.github.langramming.database.repository;

import com.github.langramming.database.model.ArtistEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    Optional<ArtistEntity> findByProviderAndProviderArtistId(
            @Nonnull String trackProviderId,
            @Nonnull String artistId);
    Collection<ArtistEntity> findByProviderAndProviderArtistIdIn(
            @Nonnull String trackProviderId,
            @Nonnull Collection<String> artistIds);


    default Optional<ArtistEntity> findById(
            @Nonnull TrackProviderType trackProviderType,
            @Nonnull String artistId) {
        return findByProviderAndProviderArtistId(trackProviderType.getId(), artistId);
    }
    default Collection<ArtistEntity> findByIds(
            @Nonnull TrackProviderType trackProviderType,
            @Nonnull Collection<String> artistIds) {
        return findByProviderAndProviderArtistIdIn(trackProviderType.getId(), artistIds);
    }

}
