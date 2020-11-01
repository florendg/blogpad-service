package com.weirdduke.blogpad.health.boundary;

import com.weirdduke.blogpad.Configuration;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthResourceIT {

    private HealthResourceClient client;

    @BeforeEach
    void init() {
        var uri = Configuration.getValue("admin.uri");
        client = RestClientBuilder.newBuilder()
                .baseUri(URI.create(uri))
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
