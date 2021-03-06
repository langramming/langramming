package dev.nickrobson.langramming.httpserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nickrobson.langramming.util.JsonUtil;
import dev.nickrobson.langramming.util.ResponseHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontendResource {

    private final FrontendService frontendService;
    private final FrontendModelProvider frontendModelProvider;
    private final ResponseHelper responseHelper;
    private final ObjectMapper objectMapper;

    @Inject
    public FrontendResource(
        FrontendService frontendService,
        FrontendModelProvider frontendModelProvider,
        ResponseHelper responseHelper
    ) {
        this.frontendService = frontendService;
        this.frontendModelProvider = frontendModelProvider;
        this.responseHelper = responseHelper;

        this.objectMapper = JsonUtil.createObjectMapper();
    }

    @GetMapping("/")
    public String getIndex() {
        return "forward:/assets/index.html";
    }

    @GetMapping("/assets/{filename}")
    public ResponseEntity<?> getAsset(
        @PathVariable("filename") String asset,
        HttpServletResponse response
    ) throws IOException {
        if (frontendService.isFrontendServerEnabled()) {
            Optional<ResponseEntity<?>> responseEntityOpt = frontendService.fromFrontendServer(
                asset,
                response
            );
            if (responseEntityOpt.isPresent()) {
                return filterResponseEntity(asset, responseEntityOpt.get());
            }
            return null;
        } else {
            ResponseEntity<?> responseEntity = frontendService.fromClasspath(asset);
            return filterResponseEntity(asset, responseEntity);
        }
    }

    private ResponseEntity<?> filterResponseEntity(String asset, ResponseEntity<?> responseEntity)
        throws JsonProcessingException {
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
        FrontendModel frontendModel = frontendModelProvider.getFrontendModel();
        String jsonObject = objectMapper.writeValueAsString(frontendModel);

        return template.replace(
            "<script type=\"application/javascript\" id=\"langramming-data\"></script>",
            "<script type=\"application/javascript\">window.__LANGRAMMING_DATA__ = " +
            jsonObject +
            "</script>"
        );
    }
}
