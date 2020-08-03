export type TrackProvider = "spotify";

export interface TrackAlbum {
  id: string;
  name: string;
}

export interface TrackArtist {
  id: string;
  name: string;
}

export interface Track {
  provider: TrackProvider;
  id: string;
  name: string;
  album: TrackAlbum[];
  artist: TrackArtist[];
}
