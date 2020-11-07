package dev.nickrobson.langramming.database.repository;

import dev.nickrobson.langramming.database.model.SpotifyUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotifyUserRepository extends JpaRepository<SpotifyUserEntity, Long> {
    Optional<SpotifyUserEntity> findByUserId(long id);
}
