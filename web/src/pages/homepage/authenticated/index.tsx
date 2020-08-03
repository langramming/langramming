import * as React from "react";

import { useAppContext } from "../../../common/hooks/useAppContext";
import { SpotifyLoginButton } from "../../../components/SpotifyLoginButton";
import { LanguageSelect } from "../../../components/LanguageSelect/LanguageSelect";

export const AuthenticatedHomepage = (): JSX.Element => {
  const appContext = useAppContext();
  const user = appContext.user!; // eslint-disable-line @typescript-eslint/no-non-null-assertion

  return (
    <main>
      <p>{`Welcome to Langramming!`}</p>
      <p>{`You're logged in as ${user.name}!`}</p>
      {!user.isSpotifyAuthed && <SpotifyLoginButton />}
      {user.isSpotifyAuthed && (
        <LanguageSelect
          onChange={(value) => {
            alert(JSON.stringify(value));
          }}
        />
      )}
    </main>
  );
};
