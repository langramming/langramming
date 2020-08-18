export type TrackProvider = "spotify";

export interface TrackAlbum {
  id: string;
  name: string;
}

export interface TrackArtist {
  id: string;
  name: string;
}

export interface TrackImage {
  width: number;
  height: number;
  url: string;
}

export interface Track {
  provider: TrackProvider;
  id: string;
  name: string;
  album: TrackAlbum;
  artists: TrackArtist[];
  images: TrackImage[];
}
