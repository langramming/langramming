import * as React from 'react';

import { LanguageSelect } from '../../../components/LanguageSelect/LanguageSelect';
import { useRecentTracks } from '../../../common/hooks/useRecentTracks';
import { TrackCarousel } from '../../../components/TrackCarousel';

export const SpotifyAuthed = () => {
  const recentTracks = useRecentTracks('spotify');
  const tracks = React.useMemo(() => recentTracks.data?.items ?? [], [recentTracks.data]);

  return (
    <>
      <TrackCarousel tracks={tracks} />
      <LanguageSelect
        onChange={(value) => {
          alert(JSON.stringify(value));
        }}
      />
    </>
  );
};
