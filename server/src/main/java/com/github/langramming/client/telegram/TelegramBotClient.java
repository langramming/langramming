package com.github.langramming.client.telegram;

import com.github.langramming.util.EnvironmentVariables;
import com.pengrad.telegrambot.TelegramBot;

import javax.inject.Singleton;

@Singleton
public class TelegramBotClient {

    private TelegramBot telegramBot;

    public void login() {
        String botToken = EnvironmentVariables.TELEGRAM_API_TOKEN;
        this.telegramBot = new TelegramBot(botToken);
        this.telegramBot.setUpdatesListener(new TelegramListener(this.telegramBot));
    }

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
