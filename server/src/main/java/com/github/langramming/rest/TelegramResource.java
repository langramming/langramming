package com.github.langramming.rest;

import com.github.langramming.client.telegram.TelegramBotClient;
import com.github.langramming.rest.response.TelegramDTO;
import com.pengrad.telegrambot.response.GetMeResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/telegram")
public class TelegramResource {

    private final TelegramBotClient telegramBotClient;

    @Inject
    public TelegramResource(TelegramBotClient telegramBotClient) {
        this.telegramBotClient = telegramBotClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TelegramDTO getTelegramInfo() {
        GetMeResponse botInfo = telegramBotClient.getTelegramBotInfo();

        return TelegramDTO.builder()
                .username(botInfo.user().username())
                .build();
    }

}
