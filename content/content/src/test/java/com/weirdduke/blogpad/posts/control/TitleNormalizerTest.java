package com.weirdduke.blogpad.posts.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TitleNormalizerTest {

    private TitleNormalizer cut;

    public static TitleNormalizer create() {
        var test = new TitleNormalizerTest();
        test.init();
        return test.cut;
    }

    @BeforeEach
    void init() {
        cut = new TitleNormalizer();
        cut.titleSeparator = "-";
        cut.init();
    }

    @Test
    void replaceInvalidCharacter() {
        String input = "Hello%World";
        assertEquals("Hello-World",cut.normalize(input));
    }
}