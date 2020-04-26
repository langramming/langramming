import * as React from 'react';

export const TelegramLoginButton = () => {
    const [ref, setRef] = React.useState<HTMLDivElement | null>(null);

    React.useEffect(() => {
        if (ref) {
            const script = document.createElement('script');
            script.src = 'https://telegram.org/js/telegram-widget.js?8';
            script.setAttribute('data-telegram-login', 'langramming_bot');
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
    }, [ref]);

    return (
      <div ref={setRef} />
    );
};