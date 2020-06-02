package com.github.langramming.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TelegramUser")
@Table(name = "telegram_user_v1")
public class TelegramUserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @Column(name = "telegram_id", unique = true)
    public Long telegramId;

    public String name;

}
