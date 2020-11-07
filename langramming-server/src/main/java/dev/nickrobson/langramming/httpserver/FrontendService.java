package dev.nickrobson.langramming.httpserver;

import dev.nickrobson.langramming.configuration.LangrammingFrontendConfiguration;
import dev.nickrobson.langramming.util.StreamUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Singleton
public class FrontendService {
    private static final Pattern ASSET_NAME = Pattern.compile(
        "^(?:.*/)?[a-z0-9._-]+\\.(html|js|css|svg)$",
        Pattern.CASE_INSENSITIVE
    );

    private final LangrammingFrontendConfiguration frontendConfiguration;

    @Inject
    public FrontendService(LangrammingFrontendConfiguration frontendConfiguration) {
        this.frontendConfiguration = frontendConfiguration;
    }

    public boolean isFrontendServerEnabled() {
        return frontendConfiguration.getPort().isPresent();
    }

    public ResponseEntity<?> fromClasspath(String asset) throws IOException {
        Matcher matcher = ASSET_NAME.matcher(asset);
        if (!matcher.matches() || asset.contains("..")) {
            return ResponseEntity.notFound().build();
        }

        try (InputStream inputStream = getClass().getResourceAsStream("/assets/" + asset)) {
            if (inputStream != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
                StreamUtil.copy(inputStream, baos);

                MediaType mediaType = guessContentTypeForExt(matcher.group(1));
                if (mediaType.getCharset() == null) {
                    mediaType =
                        new MediaType(
                            mediaType.getType(),
                            mediaType.getSubtype(),
                            StandardCharsets.UTF_8
                        );
                }

                return ResponseEntity.ok().contentType(mediaType).body(baos.toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    public Optional<ResponseEntity<?>> fromFrontendServer(
        String asset,
        HttpServletResponse response
    )
        throws IOException {
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

            return Optional.of(ResponseEntity.ok().contentType(mediaType).body(baos.toString()));
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

    private MediaType guessContentTypeForExt(String ext) {
        switch (ext) {
            case "html":
                return new MediaType("text", "html", StandardCharsets.UTF_8);
            case "js":
                return new MediaType("text", "javascript", StandardCharsets.UTF_8);
            case "css":
                return new MediaType("text", "css", StandardCharsets.UTF_8);
            case "svg":
                return new MediaType("image", "svg", StandardCharsets.UTF_8);
            default:
                return MediaType.TEXT_PLAIN;
        }
    }
}
