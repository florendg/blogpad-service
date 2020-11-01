package com.weirdduke.blogpad.metrics.boundary;

import com.weirdduke.blogpad.Configuration;
import com.weirdduke.blogpad.posts.boundary.PostResourceIT;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class MetricsResourceIT {

    private MetricsResourceClient client;

    @BeforeAll
    static void initMetricsWithBusinessCall() {
        var test = new PostResourceIT();
        test.init();
        test.shouldSavePostAndFindItAgain();
    }

    @BeforeEach
    void init() {
        var uri = Configuration.getValue("admin.uri");
        this.client = RestClientBuilder
                .newBuilder()
                .baseUri(URI.create(uri))
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
