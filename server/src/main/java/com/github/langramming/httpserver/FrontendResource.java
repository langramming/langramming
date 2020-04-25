package com.github.langramming.httpserver;

import com.github.langramming.util.EnvironmentVariables;
import com.github.langramming.util.StreamUtil;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URLConnection;

@Singleton
public class FrontendResource extends HttpHandler {

    private final FrontendService frontendService;

    @Inject
    public FrontendResource(
            FrontendService frontendService
    ) {
        this.frontendService = frontendService;
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
                        StreamUtil.copy(connection.getInputStream(), response.getOutputStream());
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
                        StreamUtil.copy(inputStream, response.getOutputStream());
                    },
                    () -> {
                        response.setStatus(HttpStatus.NOT_FOUND_404);
                        response.setContentType(MediaType.TEXT_PLAIN);
                        StreamUtil.copy(new ByteArrayInputStream("Page not found!".getBytes()), response.getOutputStream());
                    }
            );
        }
    }
}
