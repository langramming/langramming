package com.github.langramming.client.telegram;

import com.github.langramming.util.EnvironmentVariables;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;

public class TelegramBotClient {

    public static void start() {
        String botToken = EnvironmentVariables.TELEGRAM_API_TOKEN;
        TelegramBot bot = new TelegramBot(botToken);
        bot.setUpdatesListener(new TelegramListener(bot));

        System.out.println("[Telegram] Logged in as @" + bot.execute(new GetMe()).user().username());
    }

}
