package com.github.langramming.httpserver;

import com.github.langramming.configuration.LangrammingFrontendConfiguration;
import com.github.langramming.util.StreamUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Singleton
public class FrontendService {

    private final LangrammingFrontendConfiguration frontendConfiguration;

    @Inject
    public FrontendService(LangrammingFrontendConfiguration frontendConfiguration) {
        this.frontendConfiguration = frontendConfiguration;
    }

    public boolean isFrontendServerEnabled() {
        return frontendConfiguration.getPort().isPresent();
    }

    public ResponseEntity<?> fromClasspath(String asset) throws IOException {
        String assetPath = "/assets/" + asset;
        if (assetPath.contains("..")) {
            return ResponseEntity.notFound().build();
        }

        try (InputStream inputStream = getClass().getResourceAsStream(assetPath)) {
            if (inputStream != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
                StreamUtil.copy(inputStream, baos);

                String contentType = URLConnection.guessContentTypeFromName(asset);
                MediaType mediaType = MediaType.parseMediaType(contentType);
                if (mediaType.getCharset() == null) {
                    mediaType = new MediaType(mediaType.getType(), mediaType.getSubtype(), StandardCharsets.UTF_8);
                }

                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(baos.toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    public Optional<ResponseEntity<?>> fromFrontendServer(String asset, HttpServletResponse response) throws IOException {
        URL resourceUrl = getFrontendServerUrl(asset);
        if (resourceUrl == null) {
            return Optional.of(this.fromClasspath(asset));
        }

        HttpURLConnection connection = (HttpURLConnection) resourceUrl.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == 404) {
            return Optional.of(ResponseEntity.notFound().build());
        }

        String contentType = connection.getContentType();

        if (asset.equals("index.html")) {
            // copy the response and return it so it can be filtered
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

            StreamUtil.copy(connection.getInputStream(), baos);

            MediaType mediaType = MediaType.parseMediaType(contentType);

            return Optional.of(
                    ResponseEntity.ok()
                            .contentType(mediaType)
                            .body(baos.toString())
            );
        } else {
            response.setStatus(connection.getResponseCode());
            response.setContentType(contentType);

            // hot path: directly copy the frontend response into the response
            // we shouldn't do any filtering here, as it will really slow down load times
            StreamUtil.copy(1024 * 512, connection.getInputStream(), response.getOutputStream());
            return Optional.empty();
        }
    }

    private URL getFrontendServerUrl(String path) throws MalformedURLException {
        if (frontendConfiguration.getPort().isEmpty()) {
            return null;
        }

        return new URL(
                String.format(
                        "http://localhost:%d/assets/%s",
                        frontendConfiguration.getPort().get(),
                        path
                )
        );
    }

}
