package com.weirdduke.blogpad.metrics.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class MetricsResourceIT {

    private MetricsResourceClient client;

    @BeforeEach
    void init() {
        URI uri = URI.create("http://localhost:8080");
        this.client = RestClientBuilder
                .newBuilder()
                .baseUri(uri)
                .build(MetricsResourceClient.class);
    }

    @Test
    void metrics() {
        var metrics = this.client.metrics();
        assertFalse(metrics.isEmpty());
    }

    @Test
    void applicationMetrics() {
        var metrics = this.client.applicationMetrics();
        assertNotNull(metrics);

        assertFalse(metrics.isEmpty());
        System.out.println("metrics from server: " + metrics);
        int saveInvocationCounter = metrics.getJsonNumber("com.weirdduke.blogpad.posts.boundary.PostResource.update")
                .intValue();
        assertTrue(saveInvocationCounter >= 0);
    }
}
