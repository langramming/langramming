package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackMetadataEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackMetadataRepository
    extends JpaRepository<TrackMetadataEntity, Long> {
    List<TrackMetadataEntity> findAllByTrackId(long trackId);
}
