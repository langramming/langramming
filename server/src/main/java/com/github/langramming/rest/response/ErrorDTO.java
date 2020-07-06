package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@JsonAutoDetect
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDTO {
    @JsonProperty("message")
    public String message;

    public static ErrorDTO of(String message) {
        return new ErrorDTO(message);
    }
}
