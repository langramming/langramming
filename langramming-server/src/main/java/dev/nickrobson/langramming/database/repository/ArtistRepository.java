package dev.nickrobson.langramming.database.repository;

import dev.nickrobson.langramming.database.model.ArtistEntity;
import dev.nickrobson.langramming.model.TrackProviderType;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    Optional<ArtistEntity> findByProviderAndProviderArtistId(
        @Nonnull String trackProviderId,
        @Nonnull String artistId
    );

    Collection<ArtistEntity> findByProviderAndProviderArtistIdIn(
        @Nonnull String trackProviderId,
        @Nonnull Collection<String> artistIds
    );

    default Optional<ArtistEntity> findById(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull String artistId
    ) {
        return findByProviderAndProviderArtistId(trackProviderType.getId(), artistId);
    }

    default Collection<ArtistEntity> findByIds(
        @Nonnull TrackProviderType trackProviderType,
        @Nonnull Collection<String> artistIds
    ) {
        return findByProviderAndProviderArtistIdIn(trackProviderType.getId(), artistIds);
    }
}
