package com.github.langramming.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotifyUser {

    private final long id;
    private final long userId;
    private final long expiresAt;
    private final String accessToken;
    private final String refreshToken;

}
