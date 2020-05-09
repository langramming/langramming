package com.github.langramming.service;

import com.github.langramming.database.model.LanguageEntity;
import com.github.langramming.database.repository.LanguageRepository;
import com.github.langramming.model.Language;
import org.springframework.data.domain.Example;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class LanguageService {

    @Inject
    private LanguageRepository languageRepository;

    public List<Language> getLanguages() {
        return languageRepository.findAll()
                .stream()
                .map(this::toLanguage)
                .collect(Collectors.toList());
    }

    public Optional<Language> getLanguageByCode(String code) {
        LanguageEntity exampleEntity = new LanguageEntity();
        exampleEntity.code = code;

        return languageRepository.findOne(Example.of(exampleEntity))
                .map(this::toLanguage);
    }

    public Language createLanguage(String code, String name) {
        LanguageEntity newLanguageEntity = new LanguageEntity();
        newLanguageEntity.code = code;
        newLanguageEntity.name = name;

        return toLanguage(languageRepository.save(newLanguageEntity));
    }

    private Language toLanguage(LanguageEntity languageEntity) {
        return Language.builder()
                .id(languageEntity.id)
                .code(languageEntity.code)
                .name(languageEntity.name)
                .build();
    }
}
