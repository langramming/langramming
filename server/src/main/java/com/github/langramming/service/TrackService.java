package com.github.langramming.service;

import com.github.langramming.model.TrackDetails;
import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Singleton
public class TrackService {

    private final EnumMap<TrackProviderType, TrackProvider> trackProviderEnumMap;

    @Inject
    public TrackService(List<TrackProvider> trackProviderList) {
        this.trackProviderEnumMap = new EnumMap<>(TrackProviderType.class);

        trackProviderList.forEach(provider ->
                trackProviderEnumMap.put(provider.getType(), provider));
    }

    @Nonnull
    public Optional<TrackDetails> getTrackDetails(@Nonnull TrackProviderType trackProviderType, @Nonnull String trackId) {
        // TODO: add database caching layer

        return Optional.ofNullable(trackProviderEnumMap.get(trackProviderType))
                .flatMap(trackProvider -> trackProvider.getTrackDetails(trackId));
    }

}
