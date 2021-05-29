import * as React from 'react';
import { useAppContext } from '../common/hooks/useAppContext';

export const TelegramLoginButton = (): JSX.Element => {
  const [ref, setRef] = React.useState<HTMLDivElement | null>(null);
  const {
    baseUrl,
    telegram: { username: telegramBotUsername },
  } = useAppContext();

  React.useEffect(() => {
    if (ref) {
      const script = document.createElement('script');
      script.src = 'https://telegram.org/js/telegram-widget.js?8';
      script.setAttribute('data-telegram-login', telegramBotUsername);
      script.setAttribute('data-size', 'large');
      script.setAttribute('data-auth-url', `${baseUrl}/api/auth/telegram/login`);
      script.setAttribute('data-request-access', 'write');
      ref.appendChild(script);

      return () => {
        ref.removeChild(script);
      };
    }

    // if no ref, nothing to clean up
    return undefined;
  }, [baseUrl, ref, telegramBotUsername]);

  return <div ref={setRef} />;
};
