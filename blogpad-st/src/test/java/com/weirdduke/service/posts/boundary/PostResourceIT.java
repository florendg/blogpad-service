package com.weirdduke.service.posts.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostResourceIT {

    PostResourceClient client;

    @BeforeEach
    public void init() {
        URI uri = URI.create("http://localhost:8080/blogpad/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(PostResourceClient.class);
    }

    @Test
    void shouldSavePostAndFindItAgain() {
        String title = "put_st";
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        Response response = client.savePost(post);
        assertEquals(201, response.getStatus());
        assertEquals("http://localhost:8080/blogpad/resources/posts/put-st",response.getHeaderString("Location"));

        response = client.findPost(title);
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldNotSaveInvalidPost() {
        String title = "/";
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        Response response = client.savePost(post);
        assertEquals(201, response.getStatus());
    }
}
