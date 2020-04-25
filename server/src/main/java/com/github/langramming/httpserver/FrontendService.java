package com.github.langramming.httpserver;

import com.github.langramming.util.EnvironmentVariables;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Singleton
public class FrontendService {

    private Optional<Integer> frontendPort;

    @Inject
    public FrontendService() {
        Optional<String> frontendPortString = EnvironmentVariables.FRONTEND_PORT;
        this.frontendPort = frontendPortString.map(Integer::parseInt);
    }

    public void getAssetFromResources(String asset, IOConsumer<InputStream> ifExists, IORunnable ifNotExists) throws IOException {
        String assetPath = "/assets/" + asset;
        if (assetPath.contains("..") || assetPath.contains(".")) {
            ifNotExists.run();
            return;
        }
        try (InputStream inputStream = getClass().getResourceAsStream(assetPath)) {
            if (inputStream != null) {
                ifExists.accept(inputStream);
            } else {
                ifNotExists.run();
            }
        }
    }

    public void getAssetFromFrontendServer(String asset, IOConsumer<HttpURLConnection> ifExists, IORunnable ifNotExists) throws IOException {
        URL resourceUrl = getFrontendServerUrl(asset);
        if (resourceUrl == null) {
            ifNotExists.run();
            return;
        }

        HttpURLConnection connection = (HttpURLConnection) resourceUrl.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == 404 && !"/index.html".equals(asset)) {
            resourceUrl = getFrontendServerUrl(asset);
            if (resourceUrl != null) {
                connection = (HttpURLConnection) resourceUrl.openConnection();
            }
        }

        ifExists.accept(connection);
    }

    private URL getFrontendServerUrl(String path) throws MalformedURLException {
        if (frontendPort.isEmpty()) {
            return null;
        }

        return new URL(
                String.format(
                        "http://localhost:%d/%s",
                        frontendPort.get(),
                        path
                )
        );
    }

    public interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }

    public interface IORunnable {
        void run() throws IOException;
    }

}
