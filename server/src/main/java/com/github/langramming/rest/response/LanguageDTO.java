package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

@Builder
@JsonAutoDetect
public class LanguageDTO {

    public String code;
    public String name;

}
