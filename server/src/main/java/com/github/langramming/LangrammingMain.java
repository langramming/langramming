package com.github.langramming;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class LangrammingMain {
    private static final int DEFAULT_PORT = 8080;

    public static HttpServer startServer(int port) {
        // Our REST endpoints live in the .rest package: this loads them automagically.
        final ResourceConfig rc = new ResourceConfig().packages("com.github.langramming.rest");

        URI baseUri = URI.create(String.format("http://localhost:%d", port));
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, rc);
    }

    public static void main(String[] args) {
        int serverPort = Integer.getInteger(System.getenv("SERVER_PORT"), DEFAULT_PORT);
        final HttpServer server = startServer(serverPort);

        server.getListeners().stream().findFirst().ifPresentOrElse(
                networkListener -> {
                    System.out.format("Server started at :%d\n", networkListener.getPort());
                    System.out.format(
                            "WADL at %s:%d/application.wadl\n",
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

