package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pengrad.telegrambot.response.GetMeResponse;

@JsonAutoDetect
public class TelegramDTO {
    @JsonProperty("username")
    public final String username;

    public TelegramDTO(GetMeResponse user) {
        this.username = user.user().username();
    }
}
