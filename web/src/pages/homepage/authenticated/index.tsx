import * as React from "react";

import { useAppContext } from "../../../common/context/useAppContext";
import { SpotifyLoginButton } from "../../../components/SpotifyLoginButton";

export const AuthenticatedHomepage = (): JSX.Element => {
  const appContext = useAppContext();

  return (
    <main>
      <p>{`Welcome to Langramming!`}</p>
      <p>{`You're logged in as ${appContext.user?.name}!`}</p>
      {!appContext.user?.isSpotifyAuthed && <SpotifyLoginButton />}
    </main>
  );
};
