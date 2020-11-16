package com.weirdduke.reactor.posts.control;

import org.junit.jupiter.api.Test;

class OsTest {

    @Test
    void shouldRunOnMac() {
        System.out.println(System.getProperty("os.arch"));
    }
}
