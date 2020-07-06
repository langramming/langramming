package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.Language;

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
