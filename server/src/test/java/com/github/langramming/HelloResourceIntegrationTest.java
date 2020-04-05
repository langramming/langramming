package com.github.langramming;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.grizzly.http.server.NetworkListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloResourceIntegrationTest {

    private HttpServer server;
    private WebTarget target;

    @Before
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

    @After
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void hello__should_return_hello_world_response() {
        String responseMsg = target.path("hello").request().get(String.class);
        assertEquals("Hello world!", responseMsg);
    }
}
