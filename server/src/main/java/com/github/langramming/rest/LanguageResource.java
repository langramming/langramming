package com.github.langramming.rest;

import static org.apache.http.util.TextUtils.isBlank;

import com.github.langramming.model.Language;
import com.github.langramming.rest.request.LanguageRequest;
import com.github.langramming.rest.response.LanguageDTO;
import com.github.langramming.rest.response.LanguagesDTO;
import com.github.langramming.service.LanguageService;
import com.github.langramming.util.ResponseHelper;
import io.atlassian.fugue.Option;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/language")
public class LanguageResource {
    private final LanguageService languageService;
    private final ResponseHelper responseHelper;

    @Inject
    public LanguageResource(LanguageService languageService, ResponseHelper responseHelper) {
        this.languageService = languageService;
        this.responseHelper = responseHelper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLanguages() {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        List<Language> languages = languageService.getLanguages();

        return responseHelper.ok(new LanguagesDTO(languages));
    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLanguage(@PathVariable("code") String code) {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        if (isBlank(code)) {
            return responseHelper.badRequest();
        }

        Optional<Language> languageOpt = languageService.getLanguageByCode(code);

        return Option
            .fromOptional(languageOpt)
            .map(LanguageDTO::new)
            .fold(responseHelper::notFound, responseHelper::ok);
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createLanguage(@RequestBody LanguageRequest languageRequest) {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        if (isBlank(languageRequest.code) || isBlank(languageRequest.name)) {
            return responseHelper.badRequest();
        }

        Language language = languageService.createLanguage(
            languageRequest.code,
            languageRequest.name
        );
        return responseHelper.ok(new LanguageDTO(language));
    }
}
