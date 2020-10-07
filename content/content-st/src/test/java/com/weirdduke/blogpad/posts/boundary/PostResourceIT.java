package com.weirdduke.blogpad.posts.boundary;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class PostResourceIT {

    PostResourceClient client;

    @BeforeEach
    public void init() {
        URI uri = URI.create("http://localhost:8080/content/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(PostResourceClient.class);
    }

    @Test
    public void shouldSavePostAndFindItAgain() {
        String title = "put_st" + System.currentTimeMillis();
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        Response response = client.createNew(post);
        assertEquals(201, response.getStatus());

        response = client.findPost(title);
        assertEquals(200, response.getStatus());
        var fetchedPost = response.readEntity(JsonObject.class);
        assertNotNull(fetchedPost.getString("createdAt", null));
        assertNull(fetchedPost.getString("modifiedAt", null));
    }

    @Test
    public void shouldNotSaveInvalidPost() {
        String title = "ho";
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        assertEquals("HTTP 400 Bad Request",
                assertThrows(WebApplicationException.class, () -> client.createNew(post)).getMessage());
    }

    @Test
    void unknownTitleShouldYieldStatus204() {
        String title = "unknown" + System.nanoTime();
        Response response = client.findPost(title);
        assertEquals(204,response.getStatus());
    }

    @Test
    void shouldFindInitialPost() {
        Response response = client.findPost("initial");
        assertEquals(200, response.getStatus());
        JsonObject post = response.readEntity(JsonObject.class);
        assertEquals("initial",post.getString("title"));
    }
}
