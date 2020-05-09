package com.github.langramming.httpserver;

import com.github.langramming.configuration.LangrammingFrontendConfiguration;
import com.github.langramming.util.StreamUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Singleton
public class FrontendService {

    private final LangrammingFrontendConfiguration frontendConfiguration;

    @Inject
    public FrontendService(LangrammingFrontendConfiguration frontendConfiguration) {
        this.frontendConfiguration = frontendConfiguration;
    }

    public ResponseEntity<?> getFrontendAsset(String asset) throws IOException {
        if (frontendConfiguration.getPort().isPresent()) {
            return fromFrontendServer(asset);
        }
        return fromClasspath(asset);
    }

    private ResponseEntity<?> fromClasspath(String asset) throws IOException {
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

                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(baos.toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    private ResponseEntity<?> fromFrontendServer(String asset) throws IOException {
        URL resourceUrl = getFrontendServerUrl(asset);
        if (resourceUrl == null) {
            return this.fromClasspath(asset);
        }

        HttpURLConnection connection = (HttpURLConnection) resourceUrl.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == 404) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        StreamUtil.copy(connection.getInputStream(), baos);

        String contentType = connection.getContentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(baos.toString());
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
