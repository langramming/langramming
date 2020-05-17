package com.github.langramming.service;

import com.github.langramming.model.TrackProvider;
import com.github.langramming.model.TrackProviderType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumMap;
import java.util.List;

@Singleton
public class TrackService {

    private final EnumMap<TrackProviderType, TrackProvider> trackProviderEnumMap;

    @Inject
    public TrackService(List<TrackProvider> trackProviderList) {
        this.trackProviderEnumMap = new EnumMap<>(TrackProviderType.class);

        trackProviderList.forEach(provider ->
                trackProviderEnumMap.put(provider.getType(), provider));
    }

}
