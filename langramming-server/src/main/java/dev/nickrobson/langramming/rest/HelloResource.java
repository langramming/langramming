package dev.nickrobson.langramming.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example resource: this is exposed at "/api/hello"
 */
@RestController
@RequestMapping("/api/hello")
public class HelloResource {

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String sayHelloWorld() {
        // go to localhost:8080/hello to see this!
        return "Hello world!";
    }
}
