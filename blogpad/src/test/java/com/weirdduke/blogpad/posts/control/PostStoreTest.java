package com.weirdduke.blogpad.posts.control;


import com.weirdduke.blogpad.posts.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostStoreTest {

    PostStore cut;

    @BeforeEach
    void setup() {
        cut = new PostStore();
    }

    @Test
    void serializePost() {
        String serialized = cut.serialize(new Post("Hello", "World"));
        assertNotNull(serialized);
    }

    @Test
    void writeStringToFile() throws IOException {
        cut.write("target/firstPost","Content");
    }

}