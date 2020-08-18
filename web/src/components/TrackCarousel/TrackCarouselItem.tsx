import * as React from "react";

import { RecentTrack } from "../../types/RecentTracks";

interface TrackCarouselItemProps {
  track: RecentTrack;
}

export const TrackCarouselItem = ({ track }: TrackCarouselItemProps) => {
  return <pre>{JSON.stringify(track)}</pre>;
};
