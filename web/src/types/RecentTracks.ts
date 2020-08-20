import { Track } from './Track';

export interface RecentTrack {
  track: Track;
  playedAt: string;
  previewUrl: string;
}

export interface RecentTracksResponse {
  first: string;
  last: string;
  total: number;
  items: RecentTrack[];
}
