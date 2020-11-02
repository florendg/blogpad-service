package com.weirdduke.reactor.posts.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReactorTest {

    @Test
    void render() {
        assertEquals("Duke42", new Renderer().render("", ""));
    }
}