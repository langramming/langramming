package dev.nickrobson.langramming.rest;

import static org.apache.http.util.TextUtils.isBlank;

import dev.nickrobson.langramming.manager.LanguageManager;
import dev.nickrobson.langramming.model.Language;
import dev.nickrobson.langramming.rest.request.LanguageRequest;
import dev.nickrobson.langramming.rest.response.LanguageDTO;
import dev.nickrobson.langramming.rest.response.LanguagesDTO;
import dev.nickrobson.langramming.util.ResponseHelper;
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

    private final LanguageManager languageManager;
    private final ResponseHelper responseHelper;

    @Inject
    public LanguageResource(LanguageManager languageManager, ResponseHelper responseHelper) {
        this.languageManager = languageManager;
        this.responseHelper = responseHelper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLanguages() {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        List<Language> languages = languageManager.getLanguages();

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

        Optional<Language> languageOpt = languageManager.getLanguageByCode(code);

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

        Language language = languageManager.createLanguage(
            languageRequest.code,
            languageRequest.name
        );
        return responseHelper.ok(new LanguageDTO(language));
    }
}
