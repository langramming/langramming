package com.github.langramming.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SpotifyUser")
@Table(name = "spotify_user_v1")
public class SpotifyUserEntity {
    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    @JoinColumn(name = "telegram_user_id", unique = true)
    public TelegramUserEntity telegram_user;

    public String token_type;

    public String scope;

    public String access_token;

    public String refresh_token;

    public Long expires_at;
}
