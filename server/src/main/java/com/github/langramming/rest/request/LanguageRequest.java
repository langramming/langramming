package com.github.langramming.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;

@AllArgsConstructor(onConstructor_ = { @JsonCreator })
public class LanguageRequest {

    @Nonnull
    public String code;

    @Nonnull
    public String name;

}
