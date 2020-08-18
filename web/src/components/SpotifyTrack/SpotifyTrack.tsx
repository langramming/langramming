import * as React from "react";
import { Track } from "../../types/Track";

interface SpotifyTrackProps {
  track: Track;
  previewUrl: string;
}

export const SpotifyTrack = ({ track, previewUrl }: SpotifyTrackProps) => (
  <div>
    <iframe
      src={`https://open.spotify.com/embed/track/${track.id}`}
      width="300"
      height="80"
      frameBorder="0"
      allowTransparency
      allow="encrypted-media"
    />
  </div>
);
