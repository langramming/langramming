package com.github.langramming.httpserver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.User;
import lombok.Builder;

import javax.annotation.Nullable;

@Builder
@JsonAutoDetect
public class FrontendModel {

    @Nullable
    @JsonProperty("user")
    public final User user;

}
