package com.weirdduke.blogpad.posts.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class TitleNormalizer {

    @Inject
    @ConfigProperty(name = "title.separator",defaultValue = "-")
    String titleSeparator;

    int codePointSeparator;

    @PostConstruct
    public void init() {
        this.codePointSeparator = this.titleSeparator.codePoints()
                .findFirst()
                .orElseThrow();
    }

    public String normalize(String title) {
        return title.codePoints()
                .map(this::replaceWithDigitOrLetter)
                .collect(StringBuffer::new,StringBuffer::appendCodePoint,StringBuffer::append)
                .toString();
    }

    int replaceWithDigitOrLetter(int codePoint) {
        if(Character.isLetterOrDigit(codePoint)) {
            return codePoint;
        } else {
            return this.codePointSeparator;
        }
    }
}
