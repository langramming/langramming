package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SpotifyUser")
@Table(name = "spotify_user_v1")
public class SpotifyUserEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    public UserEntity user;

    @Column(name = "token_type")
    public String tokenType;

    @Column(name = "scope")
    public String scope;

    @Column(name = "access_token")
    public String accessToken;

    @Column(name = "refresh_token")
    public String refreshToken;

    @Column(name = "expires_at")
    public Long expiresAt;
}
