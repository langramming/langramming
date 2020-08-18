package com.github.langramming.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnull;

public class LanguageRequest {

    @JsonCreator
    public LanguageRequest(
        @JsonProperty("code") @Nonnull String code,
        @JsonProperty("name") @Nonnull String name
    ) {
        this.code = code;
        this.name = name;
    }

    @Nonnull
    public String code;

    @Nonnull
    public String name;
}
