package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.util.List;

@Builder
@JsonAutoDetect
public class LanguagesDTO {

    public List<LanguageDTO> languages;

}
