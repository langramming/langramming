import * as React from "react";

import { Track } from "../../types/Track";

import { TrackCarouselItem } from "./TrackCarouselItem";

interface TrackCarouselProps {
  tracks: Track[];
}

export const TrackCarousel = ({ tracks }: TrackCarouselProps) => {
  return tracks.map((track) => (
    <TrackCarouselItem key={`${track.provider}/${track.id}`} track={track} />
  ));
};
