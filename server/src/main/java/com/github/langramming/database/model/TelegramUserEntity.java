package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "TelegramUser")
@Table(name = "telegram_user_v1")
public class TelegramUserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long telegramId;

}
