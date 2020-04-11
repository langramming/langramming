package com.github.langramming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.langramming.client.telegram.TelegramBotClient;
import com.github.langramming.database.DatabaseStarter;
import com.github.langramming.httpserver.FrontendDevServerForwarder;
import com.github.langramming.httpserver.StaticAssetsHandler;
import com.github.langramming.database.DatabaseHttpServerProbe;
import com.github.langramming.util.EnvironmentVariables;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.http.server.accesslog.AccessLogBuilder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hibernate.SessionFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Optional;

public class LangrammingMain {
    private static final String NETWORK_LISTENER_NAME = "grizzly";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        TelegramBotClient.start();

        int serverPort = EnvironmentVariables.SERVER_PORT
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
        final HttpServer server = startServer(serverPort);

        NetworkListener networkListener = server.getListener(NETWORK_LISTENER_NAME);
        System.out.format("[Server] Server started at :%d\n", networkListener.getPort());
    }

    public static HttpServer startServer(int port) {
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.setMapper(new ObjectMapper());

        // Our REST endpoints live in the .rest package: this loads them automagically.
        final ResourceConfig rc = new ResourceConfig()
                .packages("com.github.langramming.rest")
                .register(jsonProvider);

        URI baseUri = URI.create(String.format("http://localhost:%d/api/", port));
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, rc, false);

        installErrorPageGenerator(server);
        installAccessLogger(server);
        installFrontendAssetsHandler(server);
        installDatabase(server);

        try {
            server.start();
        } catch (IOException ex) {
            throw new UncheckedIOException("[Server] Failed to start Grizzly server!", ex);
        }

        return server;
    }

    public static void installErrorPageGenerator(HttpServer server) {
        server.getServerConfiguration().setDefaultErrorPageGenerator(((request, status, reasonPhrase, description, exception) -> {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            request.getResponse().setContentType(MediaType.TEXT_PLAIN);
            return stringWriter.toString();
        }));
    }

    public static void installAccessLogger(HttpServer server) {
        new AccessLogBuilder("access.log")
                .instrument(server.getServerConfiguration());
    }

    public static void installFrontendAssetsHandler(HttpServer server) {
        HttpHandler httpHandler;
        Optional<String> frontendPortString = EnvironmentVariables.FRONTEND_PORT;
        if (frontendPortString.isPresent()) {
            int frontendPort = Integer.parseInt(frontendPortString.get());
            System.out.format("[Frontend] Proxying assets from the frontend server at localhost:%d\n", frontendPort);
            httpHandler = new FrontendDevServerForwarder(frontendPort);
        } else {
            System.out.println("[Frontend] Serving built frontend assets");
            httpHandler = new StaticAssetsHandler();
        }

        server.getServerConfiguration().addHttpHandler(
                httpHandler,
                "/"
        );
    }

    public static void installDatabase(HttpServer server) {
        SessionFactory sessionFactory = DatabaseStarter.initializeDatabase();

        // install database probe, which will hook into the request lifecycle
        server.getServerConfiguration().getMonitoringConfig().getWebServerConfig()
                .addProbes(new DatabaseHttpServerProbe(sessionFactory));
    }
}

