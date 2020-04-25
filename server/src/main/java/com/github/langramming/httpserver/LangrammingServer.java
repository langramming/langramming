package com.github.langramming.httpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.langramming.util.EnvironmentVariables;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

@Singleton
public class LangrammingServer {
    private static final int DEFAULT_PORT = 8080;

    @Inject
    private Injector injector;

    public void start() {
        int port = EnvironmentVariables.SERVER_PORT
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);

        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.setMapper(new ObjectMapper());

        // Our REST endpoints live in the .rest package: this loads them automagically.
        final ResourceConfig resourceConfig = new ResourceConfig()
                .register(jsonProvider);

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages("com.github.langramming")
                        .addScanners(new SubTypesScanner())
        );

        reflections.getTypesAnnotatedWith(Path.class)
                .stream()
                .filter(clazz -> clazz.getPackageName().startsWith("com.github.langramming.rest"))
                .map(injector::getInstance)
                .forEach(resourceConfig::register);

        URI baseUri = URI.create(String.format("http://localhost:%d/api/", port));
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig, false);
        reflections.getSubTypesOf(HttpHandler.class)
                .forEach(clazz -> {
                    HttpHandler handler = injector.getInstance(clazz);
                    server.getServerConfiguration().addHttpHandler(handler);
                });

        try {
            server.start();
        } catch (IOException ex) {
            throw new UncheckedIOException("[Server] Failed to start Grizzly server!", ex);
        }

        System.out.format("[Server] Server started at :%d\n", port);
    }

}
