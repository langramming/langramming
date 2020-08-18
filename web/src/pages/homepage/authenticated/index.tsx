import * as React from "react";

import { useAppContext } from "../../../common/hooks/useAppContext";
import { SpotifyAuthed } from "./SpotifyAuthed";
import { SpotifyUnauthed } from "./SpotifyUnauthed";

export const AuthenticatedHomepage = (): JSX.Element => {
  const appContext = useAppContext();
  const user = appContext.user!; // eslint-disable-line @typescript-eslint/no-non-null-assertion

  return (
    <main>
      <p>{`Welcome to Langramming!`}</p>
      <p>{`You're logged in as ${user.name}!`}</p>
      {user.isSpotifyAuthed ? <SpotifyAuthed /> : <SpotifyUnauthed />}
    </main>
  );
};
