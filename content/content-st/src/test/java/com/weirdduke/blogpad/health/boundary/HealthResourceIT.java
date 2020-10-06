package com.weirdduke.blogpad.health.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthResourceIT {

    private HealthResourceClient client;

    @BeforeEach
    void init() {
        client = RestClientBuilder.newBuilder()
                .baseUri(URI.create("http://localhost:8080"))
                .build(HealthResourceClient.class);
    }

    @Test
    void isApplicationAlive() {
        assertEquals(200,client.liveness().getStatus());
    }

    @Test
    void isApplicationReady() {
        assertEquals(200,client.liveness().getStatus());
    }
}
