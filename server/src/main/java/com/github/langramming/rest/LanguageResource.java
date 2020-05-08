package com.github.langramming.rest;

import com.github.langramming.model.Language;
import com.github.langramming.rest.request.LanguageRequest;
import com.github.langramming.rest.response.LanguageDTO;
import com.github.langramming.rest.response.LanguagesDTO;
import com.github.langramming.service.LanguageService;
import com.github.langramming.util.ResponseHelper;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.http.util.TextUtils.isBlank;

@Singleton
@Path("/language")
public class LanguageResource {

    @Inject
    private LanguageService languageService;

    @Inject
    private ResponseHelper responseHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLanguages() {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        List<LanguageDTO> languageList = languageService.getLanguages()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        LanguagesDTO languagesDTO = LanguagesDTO.builder()
                .languages(languageList)
                .build();

        return Response.ok(languagesDTO).build();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLanguage(@PathParam("code") String code) {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        if (isBlank(code)) {
            return responseHelper.badRequest();
        }

        return languageService.getLanguageByCode(code)
                .map(this::toDTO)
                .map(responseHelper::ok)
                .orElseGet(responseHelper::notFound);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLanguage(LanguageRequest languageRequest) {
        if (!responseHelper.isLoggedIn()) {
            return responseHelper.unauthorized();
        }

        if (isBlank(languageRequest.code) || isBlank(languageRequest.name)) {
            return responseHelper.badRequest();
        }

        Language language = languageService.createLanguage(languageRequest.code, languageRequest.name);
        return responseHelper.ok(toDTO(language));
    }

    private LanguageDTO toDTO(Language language) {
        return LanguageDTO.builder()
                .code(language.code)
                .name(language.name)
                .build();
    }

}
