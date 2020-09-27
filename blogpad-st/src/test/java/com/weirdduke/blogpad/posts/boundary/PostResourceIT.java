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
        URI uri = URI.create("http://localhost:8080/blogpad/resources/");
        this.client = RestClientBuilder.
                newBuilder().
                baseUri(uri).
                build(PostResourceClient.class);
    }

    @Test
    void shouldSavePostAndFindItAgain() {
        String title = "put_st" + System.currentTimeMillis();
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        Response response = client.createNew(post);
        assertEquals(201, response.getStatus());

        response = client.findPost(title);
        assertEquals(200, response.getStatus());
        var fetchedPost  = response.readEntity(JsonObject.class);
        assertNotNull(fetchedPost.getString("createdAt",null));
        assertNull(fetchedPost.getString("modifiedAt",null));
    }

    @Test
    void shouldNotSaveInvalidPost() {
        String title = "ho";
        JsonObject post = Json.createObjectBuilder()
                .add("title", title)
                .add("comment", "This is a test")
                .build();
        //Response response =
        assertEquals("HTTP 400 Bad Request",
                assertThrows(WebApplicationException.class, () -> client.createNew(post)).getMessage());
    }
}
