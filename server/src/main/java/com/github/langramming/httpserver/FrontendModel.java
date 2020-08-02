package com.github.langramming.httpserver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Builder;

@Builder
@JsonAutoDetect
public class FrontendModel {
    @JsonProperty("user")
    public final Optional<FrontendUserModel> user;

    @Builder
    @JsonAutoDetect
    public static class FrontendUserModel {
        public final long id;
        public final String name;
        public final boolean isSpotifyAuthed;
    }
}
