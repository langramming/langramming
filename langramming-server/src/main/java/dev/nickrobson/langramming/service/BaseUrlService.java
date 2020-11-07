package dev.nickrobson.langramming.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Value;

@Singleton
public class BaseUrlService {
    private final String serverUrl;

    @Inject
    public BaseUrlService(@Value("${langramming.server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getBaseUrl() {
        return serverUrl;
    }
}
