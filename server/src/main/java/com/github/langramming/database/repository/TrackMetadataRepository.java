package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackMetadataRepository extends JpaRepository<TrackMetadataEntity, Long> {
    List<TrackMetadataEntity> findAllByTrackId(long trackId);
}
