interface LangrammingData {
  user: {
    id: number;
    name: string;
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
