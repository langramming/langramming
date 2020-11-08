package dev.nickrobson.langramming.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.springframework.beans.factory.annotation.Value;

@Singleton
public class BaseUrlService {
    private final String host;
    private final String contextPath;
    private final String baseUrl;

    @Inject
    public BaseUrlService(
        @Value("${langramming.server.host}") String host,
        @Value("${server.servlet.context-path:}") String contextPath
    ) {
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        this.host = host;
        this.contextPath = contextPath;

        this.baseUrl = String.format("%s%s", host, contextPath);
    }

    public String getHost() {
        return host;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
