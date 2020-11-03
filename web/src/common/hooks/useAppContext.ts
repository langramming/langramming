interface LangrammingData {
  baseUrl: string;
  user: {
    id: number;
    name: string;
    isSpotifyAuthed: boolean;
  } | null;
}

declare global {
  interface Window {
    __LANGRAMMING_DATA__: LangrammingData;
  }
}

const langrammingData = window.__LANGRAMMING_DATA__;
export const useAppContext = (): LangrammingData => {
  return langrammingData;
};
