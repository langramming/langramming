package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "user_v1")
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @Column(name = "telegram_id", unique = true)
    public Long telegramId;

    public String name;
}
