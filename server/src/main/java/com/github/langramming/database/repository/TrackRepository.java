package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackEntity;
import com.github.langramming.model.TrackProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    Optional<TrackEntity> findByProviderAndProviderTrackId(
            @Nonnull String trackProviderId,
            @Nonnull String trackId);
    Collection<TrackEntity> findByProviderAndProviderTrackIdIn(
            @Nonnull String trackProviderId,
            @Nonnull Collection<String> trackIds);


    default Optional<TrackEntity> findById(
            @Nonnull TrackProviderType trackProviderType,
            @Nonnull String trackId) {
        return findByProviderAndProviderTrackId(trackProviderType.getId(), trackId);
    }
    default Collection<TrackEntity> findByIds(
            @Nonnull TrackProviderType trackProviderType,
            @Nonnull Collection<String> trackIds) {
        return findByProviderAndProviderTrackIdIn(trackProviderType.getId(), trackIds);
    }
}
