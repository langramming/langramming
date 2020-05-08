package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;

@JsonAutoDetect
@AllArgsConstructor
public class ErrorDTO {

    public String message;

    public static ErrorDTO of(String message) {
        return new ErrorDTO(message);
    }

}
