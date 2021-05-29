package dev.nickrobson.langramming.httpserver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.Builder;

@Builder
@JsonAutoDetect
public class FrontendModel {

    @JsonProperty("baseUrl")
    public final String baseUrl;

    @JsonProperty("user")
    public final Optional<FrontendUserModel> user;

    @JsonProperty("telegram")
    public final FrontendTelegramModel telegram;

    @Builder
    @JsonAutoDetect
    public static class FrontendUserModel {

        @JsonProperty("id")
        public final long id;

        @JsonProperty("name")
        public final String name;

        @JsonProperty("isSpotifyAuthed")
        public final boolean isSpotifyAuthed;
    }

    @Builder
    @JsonAutoDetect
    public static class FrontendTelegramModel {

        @JsonProperty("username")
        public final String username;
    }
}
