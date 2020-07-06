package com.github.langramming.database.repository;

import com.github.langramming.database.model.SpotifyUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotifyUserRepository extends JpaRepository<SpotifyUserEntity, Long> {
    Optional<SpotifyUserEntity> findByUserId(long id);
}
