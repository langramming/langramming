import * as React from "react";

import { useAppContext } from "../../utils/context/useAppContext";

export const AuthenticatedHomepage = () => {
  const appContext = useAppContext();

  return (
    <main>
      <p>Welcome to Langramming!</p>
      <p>You're logged in as {appContext.user!.name}!</p>
    </main>
  );
};
