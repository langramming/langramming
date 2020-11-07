package dev.nickrobson.langramming.database.repository;

import dev.nickrobson.langramming.database.model.TrackEntity;
import dev.nickrobson.langramming.database.model.TrackImageEntity;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackImageRepository extends JpaRepository<TrackImageEntity, Long> {
    Collection<TrackImageEntity> findAllByTrack(TrackEntity trackEntity);
}
