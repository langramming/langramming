package dev.nickrobson.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nickrobson.langramming.model.Language;

@JsonAutoDetect
public class LanguageDTO {
    @JsonProperty("code")
    public final String code;

    @JsonProperty("name")
    public final String name;

    public LanguageDTO(Language language) {
        this.code = language.code;
        this.name = language.name;
    }
}
