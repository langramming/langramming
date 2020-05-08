package com.github.langramming.client.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TelegramListener implements UpdatesListener {

    private final TelegramBot bot;

    @Override
    public int process(List<Update> updateList) {
        System.out.println("[Telegram] Received updates " + updateList);
        return CONFIRMED_UPDATES_ALL;
    }
}
