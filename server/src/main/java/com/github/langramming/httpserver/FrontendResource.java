package com.github.langramming.httpserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.langramming.model.User;
import com.github.langramming.service.UserService;
import com.github.langramming.util.EnvironmentVariables;
import com.github.langramming.util.StreamUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AllArgsConstructor;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class FrontendResource extends HttpHandler {

    private final FrontendService frontendService;
    private final UserService.UserProvider userProvider;
    private final ObjectMapper objectMapper;
    private final Cache<String, CachedAsset> templateCache;

    @Inject
    public FrontendResource(
            FrontendService frontendService,
            UserService.UserProvider userProvider
    ) {
        this.frontendService = frontendService;
        this.userProvider = userProvider;
        this.objectMapper = new ObjectMapper();
        this.templateCache = CacheBuilder.newBuilder()
                .maximumSize(20)
                .build();
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        String asset = request.getRequestURI();
        if (EnvironmentVariables.FRONTEND_PORT.isPresent()) {
            frontendService.getAssetFromFrontendServer(
                    asset,
                    connection -> {
                        response.setStatus(connection.getResponseCode());
                        response.setContentType(connection.getContentType());
                        copyAssetToResponse(asset, connection.getInputStream(), response);
                    },
                    () -> {
                        response.setStatus(HttpStatus.NOT_FOUND_404);
                        response.setContentType(MediaType.TEXT_PLAIN);
                        StreamUtil.copy(new ByteArrayInputStream("Page not found!".getBytes()), response.getOutputStream());
                    });
        } else {
            frontendService.getAssetFromResources(
                    asset,
                    inputStream -> {
                        response.setStatus(HttpStatus.OK_200);
                        response.setContentType(URLConnection.guessContentTypeFromName(asset));
                        copyAssetToResponse(asset, inputStream, response);
                    },
                    () -> {
                        response.setStatus(HttpStatus.NOT_FOUND_404);
                        response.setContentType(MediaType.TEXT_PLAIN);
                        StreamUtil.copy(new ByteArrayInputStream("Page not found!".getBytes()), response.getOutputStream());
                    }
            );
        }
    }

    private void copyAssetToResponse(String asset, InputStream inputStream, Response response) throws IOException {
        boolean isHtml = asset.endsWith(".html") || asset.endsWith("/") || !asset.contains("/");

        if (!isHtml) {
            StreamUtil.copy(inputStream, response.getOutputStream());
            return;
        }

        CachedAsset cachedAsset = templateCache.getIfPresent(asset);
        if (cachedAsset == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(8096);
            StreamUtil.copy(inputStream, baos);
            String content = baos.toString();

            cachedAsset = new CachedAsset(content);
            templateCache.put(asset, cachedAsset);
        }

        String html = replaceVariables(cachedAsset.content);

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

    @AllArgsConstructor
    private static class CachedAsset {
        private final String content;
    }
}
