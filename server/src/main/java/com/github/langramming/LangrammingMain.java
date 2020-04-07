package com.github.langramming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.langramming.httpserver.FrontendDevServerForwarder;
import com.github.langramming.httpserver.StaticAssetsHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class LangrammingMain {
    private static final int DEFAULT_PORT = 8080;

    public static HttpServer startServer(int port) {
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.setMapper(new ObjectMapper());

        // Our REST endpoints live in the .rest package: this loads them automagically.
        final ResourceConfig rc = new ResourceConfig().packages("com.github.langramming.rest");

        URI baseUri = URI.create(String.format("http://localhost:%d/api/", port));
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, rc);

        String frontendPortString = System.getenv("FRONTEND_PORT");
        if (frontendPortString != null) {
            System.out.format("Proxying assets from the frontend server at localhost:%s\n", frontendPortString);
            int frontendPort = Integer.parseInt(frontendPortString);
            server.getServerConfiguration().addHttpHandler(new FrontendDevServerForwarder(frontendPort), "/");
        } else {
            System.out.println("Serving built frontend assets");
            server.getServerConfiguration().addHttpHandler(
                    new StaticAssetsHandler(),
                    "/"
            );
        }

        return server;
    }

    public static void main(String[] args) {
        String serverPortString = System.getenv("SERVER_PORT");
        int serverPort = serverPortString != null ? Integer.parseInt(serverPortString) : DEFAULT_PORT;
        final HttpServer server = startServer(serverPort);

        server.getListeners().stream().findFirst().ifPresentOrElse(
                networkListener -> {
                    System.out.format("Server started at :%d\n", networkListener.getPort());
                    System.out.format(
                            "WADL at http://%s:%d/application.wadl\n",
                            networkListener.getHost(),
                            networkListener.getPort()
                    );
                },
                () -> {
                    System.out.println("Server started.");
                }
        );

        System.out.println("Hit Ctrl+C to stop it...");
    }
}

