import * as React from "react";

import { Track } from "../../types/Track";

interface TrackCarouselItemProps {
  track: Track;
}

export const TrackCarouselItem = ({ track }: TrackCarouselItemProps) => {
  return <pre>{JSON.stringify(track)}</pre>;
};
