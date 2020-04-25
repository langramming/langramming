import * as React from 'react';

import {useAppContext} from "../../utils/context/use-app-context";

export const AuthenticatedHomepage = () => {
    const appContext = useAppContext();

    return (
        <main>
            <p>Welcome to Langramming!</p>
            <p>You're logged in as {appContext.user!.name}!</p>
        </main>
    );
}
