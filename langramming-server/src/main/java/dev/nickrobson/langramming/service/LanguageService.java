package dev.nickrobson.langramming.service;

import dev.nickrobson.langramming.database.model.LanguageEntity;
import dev.nickrobson.langramming.database.repository.LanguageRepository;
import dev.nickrobson.langramming.model.Language;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Inject
    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getLanguages() {
        return languageRepository
            .findAll()
            .stream()
            .map(this::toLanguage)
            .collect(Collectors.toList());
    }

    public Optional<Language> getLanguageByCode(String code) {
        return languageRepository.findByCode(code).map(this::toLanguage);
    }

    public Language createLanguage(String code, String name) {
        LanguageEntity newLanguageEntity = LanguageEntity.builder().code(code).name(name).build();

        return toLanguage(languageRepository.save(newLanguageEntity));
    }

    private Language toLanguage(LanguageEntity languageEntity) {
        return Language
            .builder()
            .id(languageEntity.id)
            .code(languageEntity.code)
            .name(languageEntity.name)
            .build();
    }
}
