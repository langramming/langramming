package com.github.langramming.client.telegram;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TelegramStarter {
    @Inject
    private TelegramBotClient telegramBotClient;

    public void start() {
        telegramBotClient.login();
    }
}
