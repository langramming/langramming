package com.github.langramming.service;

import com.github.langramming.database.LangrammingDatabase;
import com.github.langramming.database.model.LanguageEntity;
import com.github.langramming.model.Language;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class LanguageService {

    @Inject
    private LangrammingDatabase database;

    public List<Language> getLanguages() {
        List<LanguageEntity> languageEntities = database.runInTransaction(
                ((session, transaction) -> {
                    CriteriaBuilder cb = session.getCriteriaBuilder();
                    CriteriaQuery<LanguageEntity> q = cb.createQuery(LanguageEntity.class);
                    return session.createQuery(
                            q.select(q.from(LanguageEntity.class))
                    ).list();
                })
        );

        return languageEntities.stream()
                .map(this::toLanguage)
                .collect(Collectors.toList());
    }

    public Optional<Language> getLanguageByCode(String code) {
        Optional<LanguageEntity> languageEntityOpt = database.runInTransaction(
                ((session, transaction) -> {
                    CriteriaBuilder cb = session.getCriteriaBuilder();
                    CriteriaQuery<LanguageEntity> q = cb.createQuery(LanguageEntity.class);
                    Root<LanguageEntity> root = q.from(LanguageEntity.class);

                    return session.createQuery(
                            q.select(root).where(
                                    cb.equal(root.get("code"), cb.literal(code))
                            )
                    ).getResultList().stream().findFirst();
                })
        );

        return languageEntityOpt.map(this::toLanguage);
    }

    public Language createLanguage(String code, String name) {
        LanguageEntity languageEntity = database.runInTransaction((session, transaction) -> {
            LanguageEntity newLanguageEntity = new LanguageEntity();
            newLanguageEntity.code = code;
            newLanguageEntity.name = name;

            session.save(newLanguageEntity);

            return newLanguageEntity;
        });

        return toLanguage(languageEntity);
    }

    private Language toLanguage(LanguageEntity languageEntity) {
        return Language.builder()
                .id(languageEntity.id)
                .code(languageEntity.code)
                .name(languageEntity.name)
                .build();
    }
}
