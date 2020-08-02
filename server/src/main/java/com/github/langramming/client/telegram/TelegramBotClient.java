package com.github.langramming.client.telegram;

import com.github.langramming.configuration.LangrammingTelegramConfiguration;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.GetMeResponse;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

@Slf4j
@Singleton
public class TelegramBotClient implements DisposableBean {
    private final LangrammingTelegramConfiguration telegramConfiguration;
    private TelegramBot telegramBot;
    private GetMeResponse telegramBotInfo;

    @Inject
    public TelegramBotClient(
        LangrammingTelegramConfiguration telegramConfiguration
    ) {
        this.telegramConfiguration = telegramConfiguration;
    }

    public void login() {
        String botToken = telegramConfiguration.getToken();
        this.telegramBot = new TelegramBot(botToken);
        this.telegramBot.setUpdatesListener(
                new TelegramListener(this.telegramBot)
            );
        this.telegramBotInfo = telegramBot.execute(new GetMe());

        log.info("Logged in as @" + telegramBotInfo.user().username());
    }

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }

    public GetMeResponse getTelegramBotInfo() {
        return telegramBotInfo;
    }

    @Override
    public void destroy() {
        this.telegramBot.removeGetUpdatesListener();
        log.info("Removed Telegram update listener");
    }
}
