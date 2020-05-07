package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "TelegramUser")
@Table(name = "telegram_user_v1", uniqueConstraints = {
        @UniqueConstraint(name = "id", columnNames = "id"),
        @UniqueConstraint(name = "telegramId", columnNames = "telegramId"),
})
public class TelegramUserEntity {

    @Id
    @GeneratedValue
    public Long id;

    public Long telegramId;

    public String name;

}
