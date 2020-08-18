import * as React from "react";

import { RecentTrack } from "../../types/RecentTracks";
import { SpotifyTrack } from "../SpotifyTrack";

interface TrackCarouselItemProps {
  track: RecentTrack;
}

export const TrackCarouselItem = ({
  track: { track, previewUrl },
}: TrackCarouselItemProps) => {
  if (track.provider !== "spotify") {
    throw new Error("not a spotify track :(");
  }

  return <SpotifyTrack track={track} previewUrl={previewUrl} />;
};
