package com.github.langramming.httpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.github.langramming.util.EnvironmentVariables;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

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

        // bridge HK2 <-> Guice so resources can have Guice services injected
        final ResourceConfig resourceConfig = new ResourceConfig() {
            {
                register(jsonProvider);
                register(new ContainerLifecycleListener() {
                    public void onStartup(Container container) {
                        ServiceLocator serviceLocator = container.getApplicationHandler().getInjectionManager().getInstance(ServiceLocator.class);
                        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
                        guiceBridge.bridgeGuiceInjector(injector);
                    }
                    public void onReload(Container container) {}
                    public void onShutdown(Container container) {}
                });
            }
        };

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages("com.github.langramming")
                        .addScanners(new SubTypesScanner())
        );

        Set<Resource> resources = reflections.getTypesAnnotatedWith(Path.class)
                .stream()
                .filter(clazz -> clazz.getPackageName().startsWith("com.github.langramming.rest"))
                .map(Resource::from)
                .collect(Collectors.toSet());

        resourceConfig.registerResources(resources);

        URI baseUri = URI.create(String.format("http://localhost:%d/api/", port));
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig, false);

        server.getServerConfiguration()
                .addHttpHandler(injector.getInstance(FrontendResource.class));

        server.getServerConfiguration().getMonitoringConfig().getWebServerConfig()
                .addProbes(injector.getInstance(UserContextFilter.class));

        try {
            server.start();
        } catch (IOException ex) {
            throw new UncheckedIOException("[Server] Failed to start Grizzly server!", ex);
        }

        System.out.format("[Server] Server started at :%d\n", port);
    }

}
