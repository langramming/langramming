import * as React from 'react';

import { RecentTrack } from '../../types/RecentTracks';

import { TrackCarouselItem } from './TrackCarouselItem';

interface TrackCarouselProps {
  tracks: RecentTrack[];
  onNext: () => void;
}

export const TrackCarousel = ({ tracks, onNext }: TrackCarouselProps) => {
  return (
    <>
      {tracks.map((track) => (
        <TrackCarouselItem key={`${track.track.provider}/${track.track.id}`} track={track} />
      ))}
      <button type="button" onClick={onNext}>
        Load next
      </button>
    </>
  );
};
