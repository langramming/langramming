package dev.nickrobson.langramming.client.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TelegramListener implements UpdatesListener {
    private final TelegramBot bot;

    @Override
    public int process(List<Update> updateList) {
        log.debug("Received updates " + updateList);
        return CONFIRMED_UPDATES_ALL;
    }
}
