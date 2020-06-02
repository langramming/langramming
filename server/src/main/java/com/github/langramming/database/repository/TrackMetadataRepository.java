package com.github.langramming.database.repository;

import com.github.langramming.database.model.TrackMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackMetadataRepository extends JpaRepository<TrackMetadataEntity, Long> {
}
