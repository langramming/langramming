import * as React from 'react';
import { useFetch } from "../fetch/useFetch";

export const TelegramLoginButton = () => {
    const [ref, setRef] = React.useState<HTMLDivElement | null>(null);
    const telegramInfo = useFetch<{ username: string }>('/api/telegram');

    React.useEffect(() => {
        if (ref && !telegramInfo.loading && telegramInfo.data) {
            const script = document.createElement('script');
            script.src = 'https://telegram.org/js/telegram-widget.js?8';
            script.setAttribute('data-telegram-login', telegramInfo.data.username);
            script.setAttribute('data-size', 'large');
            script.setAttribute('data-auth-url', `${window.location.origin}/api/auth/login`);
            script.setAttribute('data-request-access', 'write');
            ref.appendChild(script);

            return () => {
                ref.removeChild(script);
            }
        }

        // if no ref, nothing to clean up
        return () => {};
    }, [ref, telegramInfo]);

    return (
      <div ref={setRef} />
    );
};
