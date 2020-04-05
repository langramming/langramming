package com.github.langramming;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.grizzly.http.server.NetworkListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloResourceIntegrationTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() {
        // start the server
        server = LangrammingMain.startServer(0); // 0 means find any random available port

        String baseUrl;
        try {
            NetworkListener networkListener = server.getListeners().stream().findFirst().get();
            baseUrl = String.format("http://%s:%d", networkListener.getHost(), networkListener.getPort());
        } catch (Exception ex) {
            server.shutdownNow();
            throw ex;
        }

        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(baseUrl);
    }

    @AfterEach
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void hello__should_return_hello_world_response() {
        String responseMsg = target.path("hello").request().get(String.class);
        assertEquals("Hello world!", responseMsg);
    }
}
