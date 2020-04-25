package com.github.langramming.client.telegram;

import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.GetMeResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TelegramStarter {

    @Inject
    private TelegramBotClient telegramBotClient;

    public void start() {
        telegramBotClient.login();

        GetMeResponse botInfo = telegramBotClient.getTelegramBot().execute(new GetMe());
        System.out.println("[Telegram] Logged in as @" + botInfo.user().username());
    }

}
