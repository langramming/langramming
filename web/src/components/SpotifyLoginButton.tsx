import * as React from 'react';
import { useAppContext } from '../common/hooks/useAppContext';

export const SpotifyLoginButton = (): JSX.Element => {
  const { baseUrl } = useAppContext();

  return (
    <a href={`${baseUrl}/api/auth/spotify/connect`}>
      <button>{'Connect Spotify'}</button>
    </a>
  );
};
