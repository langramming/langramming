package com.github.langramming.httpserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.langramming.model.User;
import com.github.langramming.service.UserService;
import com.github.langramming.util.ResponseHelper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class FrontendResource {

    private final FrontendService frontendService;
    private final ResponseHelper responseHelper;
    private final UserService.UserProvider userProvider;
    private final ObjectMapper objectMapper;

    @Inject
    public FrontendResource(
            FrontendService frontendService,
            ResponseHelper responseHelper,
            UserService.UserProvider userProvider) {
        this.frontendService = frontendService;
        this.responseHelper = responseHelper;
        this.userProvider = userProvider;

        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/")
    public String getIndex() {
        return "forward:/assets/index.html";
    }

    @GetMapping("/assets/{filename}")
    public ResponseEntity<?> getAsset(@PathVariable("filename") String asset, HttpServletResponse response) throws IOException {
        if (frontendService.isFrontendServerEnabled()) {
            Optional<ResponseEntity<?>> responseEntityOpt = frontendService.fromFrontendServer(asset, response);
            if (responseEntityOpt.isPresent()) {
                return filterResponseEntity(asset, responseEntityOpt.get());
            }
            return null;
        } else {
            ResponseEntity<?> responseEntity = frontendService.fromClasspath(asset);
            return filterResponseEntity(asset, responseEntity);
        }
    }

    private ResponseEntity<?> filterResponseEntity(String asset, ResponseEntity<?> responseEntity) throws JsonProcessingException {
        if (asset.equals("index.html") && responseEntity.getBody() instanceof String) {
            String template = (String) responseEntity.getBody();
            String content = replaceVariables(template);
            MediaType mediaType = responseEntity.getHeaders().getContentType();
            if (mediaType == null) {
                mediaType = new MediaType("text", "html", StandardCharsets.UTF_8);
            }
            responseEntity = responseHelper.ok(content, mediaType);
        }
        return responseEntity;
    }

    private String replaceVariables(String template) throws JsonProcessingException {
        Optional<User> user = userProvider.get();

        Map<String, Object> langrammingData = new HashMap<>();
        langrammingData.put("user", user.orElse(null));

        String jsonObject = objectMapper.writeValueAsString(langrammingData);

        return template.replace(
                "<!-- {{LANGRAMMING_DATA}} -->",
                "<script>window.__LANGRAMMING_DATA__ = " + jsonObject + "</script>"
        );
    }
}
