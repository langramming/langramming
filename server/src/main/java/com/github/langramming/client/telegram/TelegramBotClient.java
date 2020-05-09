package com.github.langramming.client.telegram;

import com.github.langramming.configuration.LangrammingTelegramConfiguration;
import com.pengrad.telegrambot.TelegramBot;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TelegramBotClient {

    private final LangrammingTelegramConfiguration telegramConfiguration;
    private TelegramBot telegramBot;

    @Inject
    public TelegramBotClient(LangrammingTelegramConfiguration telegramConfiguration) {
        this.telegramConfiguration = telegramConfiguration;
    }

    public void login() {
        String botToken = telegramConfiguration.getToken();
        this.telegramBot = new TelegramBot(botToken);
        this.telegramBot.setUpdatesListener(new TelegramListener(this.telegramBot));
    }

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
