import * as React from "react";

import { RecentTrack } from "../../types/RecentTracks";

import { TrackCarouselItem } from "./TrackCarouselItem";

interface TrackCarouselProps {
  tracks: RecentTrack[];
}

export const TrackCarousel = ({ tracks }: TrackCarouselProps) => {
  return (
    <>
      {tracks.map((track) => (
        <TrackCarouselItem
          key={`${track.track.provider}/${track.track.id}`}
          track={track}
        />
      ))}
    </>
  );
};
