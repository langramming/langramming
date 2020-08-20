import * as React from 'react';

export const SpotifyLoginButton = (): JSX.Element => (
  <a href="/api/auth/spotify/connect">
    <button>{'Connect Spotify'}</button>
  </a>
);
