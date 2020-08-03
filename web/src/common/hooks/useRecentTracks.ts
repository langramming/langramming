import * as qs from "query-string";

import { RecentTracksResponse } from "../../types/RecentTracks";
import { TrackProvider } from "../../types/Track";

import { FetchState, useFetch } from "./useFetch";

interface UseRecentTracksRequest {
  before?: Date;
  after?: Date;
  limit?: number;
}

export const useRecentTracks = (
  provider: TrackProvider,
  { before, after, limit }: UseRecentTracksRequest = {}
): FetchState<RecentTracksResponse> => {
  const queryString = qs.stringify({
    before: before?.getDate(),
    after: after?.getDate(),
    limit,
  });

  return useFetch<RecentTracksResponse>(
    `/api/track/recent/${provider}?${queryString}`
  );
};
