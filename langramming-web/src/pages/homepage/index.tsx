import * as React from 'react';

import { useAppContext } from '../../common/hooks/useAppContext';

import { AuthenticatedHomepage } from './authenticated';
import { UnauthenticatedHomepage } from './unauthenticated';

export const Homepage = (): JSX.Element => {
  const appContext = useAppContext();

  if (appContext.user) {
    return <AuthenticatedHomepage />;
  }

  return <UnauthenticatedHomepage />;
};
