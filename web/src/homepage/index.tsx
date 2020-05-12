import * as React from 'react';

import { useAppContext } from "../utils/context/useAppContext";

import { AuthenticatedHomepage } from "./authenticated";
import { UnauthenticatedHomepage } from "./unauthenticated";

export const Homepage = () => {
    const appContext = useAppContext();

    if (appContext.user) {
        return <AuthenticatedHomepage />;
    }

    return <UnauthenticatedHomepage/>;
};
