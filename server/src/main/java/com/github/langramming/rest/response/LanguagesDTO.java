package com.github.langramming.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.langramming.model.Language;
import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class LanguagesDTO {
    @JsonProperty("languages")
    public List<LanguageDTO> languages;

    public LanguagesDTO(List<Language> languages) {
        this.languages = languages.stream().map(LanguageDTO::new).collect(Collectors.toList());
    }
}
