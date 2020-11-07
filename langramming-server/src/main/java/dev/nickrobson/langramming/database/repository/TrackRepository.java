package dev.nickrobson.langramming.database.repository;

import dev.nickrobson.langramming.database.model.TrackEntity;
import dev.nickrobson.langramming.model.TrackProviderType;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    Optional<TrackEntity> findByProviderAndProviderTrackId(
        @Nonnull String trackProviderId,
        @Nonnull String trackId
    );

    Collection<TrackEntity> findByProviderAndProviderTrackIdIn(
        @Nonnull String trackProviderId,
        @Nonnull Collection<String> trackIds
    );

    default Optional<TrackEntity> findById(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull String trackId
    ) {
        return findByProviderAndProviderTrackId(trackProviderType.getId(), trackId);
    }

    default Collection<TrackEntity> findByIds(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull Collection<String> trackIds
    ) {
        return findByProviderAndProviderTrackIdIn(trackProviderType.getId(), trackIds);
    }
}
