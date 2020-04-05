package com.github.langramming.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "/hello" path)
 */
@Path("/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHelloWorld() {
        // go to localhost:8080/hello to see this!
        return "Hello world!";
    }
}
