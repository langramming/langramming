package dev.nickrobson.langramming.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class User {
    private long id;
    private long telegramId;

    @Setter
    private String name;
}
