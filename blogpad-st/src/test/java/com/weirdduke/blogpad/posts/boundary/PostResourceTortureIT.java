package com.weirdduke.blogpad.posts.boundary;

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

public class PostResourceTortureIT {

    private PostResourceClient client;
    private String title;
    private ExecutorService threadPool;

    @BeforeEach
    void init() {
        var uri = URI.create("http://localhost:8080/blogpad/resources");
        this.client = RestClientBuilder.newBuilder()
                .baseUri(uri)
                .build(PostResourceClient.class);
        this.title = "torture" + System.currentTimeMillis();
        JsonObject post = Json.createObjectBuilder()
                .add("title",title)
                .add("content","for torture").build();
        Response response = client.createNew(post);
        assertEquals(201,response.getStatus());
        threadPool = Executors.newFixedThreadPool(20);
    }

    @Test
    void startTorture() {
        List<CompletableFuture<Void>> tasks = Stream.generate(this::runScenarios)
                .limit(10000)
                .collect(Collectors.toList());
        tasks.forEach(CompletableFuture::join);
    }

    CompletableFuture<Void> runScenarios() {
        return CompletableFuture.runAsync(this::findPost,this.threadPool).thenRunAsync(this::findUnknownPost,this.threadPool);
    }

    void findUnknownPost() {
        assertThrows(WebApplicationException.class,()->client.findPost("non-existing"+System.nanoTime()));
    }

    void findPost() {
        Response response = client.findPost(title);
        JsonObject post = response.readEntity(JsonObject.class);
        assertNotNull(post);
    }
}
