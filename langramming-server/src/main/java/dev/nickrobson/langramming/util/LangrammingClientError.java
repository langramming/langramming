package dev.nickrobson.langramming.util;

import lombok.Getter;

public enum LangrammingClientError {
    SPOTIFY_ACCESS_DENIED("spotify_access_denied"),
    SPOTIFY_UNKNOWN_ERROR("spotify_unknown_error");

    @Getter
    private final String code;

    LangrammingClientError(String code) {
        this.code = code;
    }
}
