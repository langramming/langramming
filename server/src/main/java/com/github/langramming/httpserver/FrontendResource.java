package com.github.langramming.httpserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.langramming.model.User;
import com.github.langramming.service.UserService;
import com.github.langramming.util.EnvironmentVariables;
import com.github.langramming.util.StreamUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class FrontendResource extends HttpServlet {

    private final FrontendService frontendService;
    private final UserService.UserProvider userProvider;
    private final ObjectMapper objectMapper;

    @Inject
    public FrontendResource(
            FrontendService frontendService,
            UserService.UserProvider userProvider
    ) {
        this.frontendService = frontendService;
        this.userProvider = userProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String asset = req.getRequestURI();
        if (EnvironmentVariables.FRONTEND_PORT.isPresent()) {
            frontendService.getAssetFromFrontendServer(
                    asset,
                    connection -> {
                        resp.setStatus(connection.getResponseCode());
                        resp.setContentType(connection.getContentType());
                        copyAssetToResponse(asset, connection.getInputStream(), resp);
                    },
                    () -> {
                        resp.setStatus(HttpStatus.NOT_FOUND.value());
                        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        StreamUtil.copy(new ByteArrayInputStream("Page not found!".getBytes()), resp.getOutputStream());
                    });
        }
    }

    private void copyAssetToResponse(String asset, InputStream inputStream, HttpServletResponse response) throws IOException {
        boolean isHtml = asset.endsWith(".html") || asset.endsWith("/") || !asset.contains("/");

        if (!isHtml) {
            StreamUtil.copy(inputStream, response.getOutputStream());
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        StreamUtil.copy(inputStream, baos);

        String html = replaceVariables(baos.toString());

        StreamUtil.copy(
                new ByteArrayInputStream(html.getBytes()),
                response.getOutputStream()
        );
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
