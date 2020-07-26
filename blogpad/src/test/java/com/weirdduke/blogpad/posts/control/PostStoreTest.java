package com.weirdduke.blogpad.posts.control;


import com.weirdduke.blogpad.posts.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostStoreTest {

    PostStore cut;

    @BeforeEach
    void setup() {
        cut = new PostStore();
        cut.storageDir = "target";
        cut.init();
    }

    @Test
    void serializePost() throws Exception {
        String serialized = cut.serialize(new Post("Hello", "World"));
        assertNotNull(serialized);
    }

    @Test
    void writeStringToFile() throws IOException {
        String expected = "Hello, Duke";
        String path = "fistPost";
        cut.write(path,expected);
        String actual = cut.readString(path);
        assertEquals(expected,actual);
    }

    @Test
    void shouldDeserializeStringIntoPost() throws Exception{
        String json = "{\"title\": \"serialized\", \"comment\": \"hello serialized\"}";
        Post post = cut.deserialize(json);
        assertEquals("serialized", post.title);
        assertEquals("hello serialized",post.comment);
    }

    @Test
    void shouldSaveAndReadPost() throws Exception {
        Post expected = new Post("demo","Hello, Duke!");
        cut.save(expected);
        Post actual = cut.read("demo");
        assertEquals(expected.title,actual.title);
        assertEquals(expected.comment, actual.comment);
    }

}