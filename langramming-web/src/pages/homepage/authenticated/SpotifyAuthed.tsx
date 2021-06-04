import * as React from 'react';

import { LanguageSelect } from '../../../components/LanguageSelect/LanguageSelect';
import { useRecentTracks } from '../../../common/hooks/useRecentTracks';
import { TrackCarousel } from '../../../components/TrackCarousel';

export const SpotifyAuthed = () => {
  const [before, setBefore] = React.useState<Date | undefined>();
  const recentTracks = useRecentTracks('spotify', { before, limit: 10 });
  const tracks = React.useMemo(() => recentTracks.data?.items ?? [], [recentTracks.data]);

  const onLoadNextTracks = React.useCallback(() => {
    const first = recentTracks.data?.first;
    setBefore(first ? new Date(parseInt(first)) : undefined);
  }, [recentTracks.data?.first]);

  return (
    <>
      <TrackCarousel tracks={tracks} onNext={onLoadNextTracks} />
      <LanguageSelect
        onChange={(value) => {
          alert(JSON.stringify(value));
        }}
      />
    </>
  );
};
