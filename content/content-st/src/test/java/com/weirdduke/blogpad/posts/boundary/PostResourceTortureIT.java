package com.weirdduke.blogpad.posts.boundary;

import com.weirdduke.blogpad.metrics.boundary.MetricsResourceClient;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PostResourceTortureIT {

    private PostResourceClient client;
    private String title;
    private ExecutorService threadPool;
    private MetricsResourceClient metricsResourceClient;

    @BeforeEach
    void init() {
        var uri = URI.create("http://localhost:8080/blogpad/resources");
        this.client = RestClientBuilder.newBuilder()
                .baseUri(uri)
                .build(PostResourceClient.class);
        this.title = "torture" + System.currentTimeMillis();
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("content", "for torture").build();
        Response response = client.createNew(post);
        assertEquals(201, response.getStatus());
        threadPool = Executors.newFixedThreadPool(20);
        initMetricsClient();
    }

    private void initMetricsClient() {
        this.metricsResourceClient = RestClientBuilder
                .newBuilder()
                .baseUri(URI.create("http://localhost:8080"))
                .build(MetricsResourceClient.class);
    }

    @Test
    void startTorture() {
        assumeTrue(System.getProperty("torture",null) != null);
        List<CompletableFuture<Void>> tasks = Stream.generate(this::runScenarios)
                .limit(500)
                .collect(Collectors.toList());
        tasks.forEach(CompletableFuture::join);
        verifyPerformance();
    }

    CompletableFuture<Void> runScenarios() {
        return CompletableFuture.runAsync(this::findPost, this.threadPool).thenRunAsync(this::findUnknownPost, this.threadPool);
    }

    void findUnknownPost() {
        Response response = client.findPost("non-existing" + System.nanoTime());
        assertEquals(204,response.getStatus());
    }

    void findPost() {
        Response response = client.findPost(title);
        JsonObject post = response.readEntity(JsonObject.class);
        assertNotNull(post);
    }

    private void verifyPerformance() {
        JsonObject json = metricsResourceClient.applicationMetrics()
                .getJsonObject("com.weirdduke.blogpad.posts.boundary.PostResource.findPost");
        double oneMinRate = json.getJsonNumber("oneMinRate").doubleValue();
        System.out.println("++++++OneMinRate: "+oneMinRate);
        assertTrue(oneMinRate > 5);
    }
}
