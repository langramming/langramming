package com.github.langramming.httpserver;

import com.github.langramming.util.StreamUtil;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FrontendDevServerForwarder extends HttpHandler {

    private final int frontendPort;

    public FrontendDevServerForwarder(int frontendPort) {
        this.frontendPort = frontendPort;
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        URL resourceUrl = getFrontendServerUrl(request.getRequestURI());

        HttpURLConnection connection = (HttpURLConnection) resourceUrl.openConnection();
        int statusCode = connection.getResponseCode();
        if (statusCode == 404 && !"/index.html".equals(request.getRequestURI())) {
            resourceUrl = getFrontendServerUrl("/index.html");
            connection = (HttpURLConnection) resourceUrl.openConnection();
        }

        response.setStatus(connection.getResponseCode());
        response.setContentType(connection.getContentType());
        response.setContentLength(connection.getContentLength());

        try (InputStream resourceStream = connection.getInputStream()) {
            try (OutputStream outputStream = response.getOutputStream()) {
                StreamUtil.copy(resourceStream, outputStream);
            }
        }
    }

    private URL getFrontendServerUrl(String requestUri) throws MalformedURLException {
        return UriBuilder.fromPath(requestUri)
                .scheme("http")
                .host("localhost")
                .port(frontendPort)
                .build()
                .toURL();
    }

}
