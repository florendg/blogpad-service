package com.weirdduke.blogpad.posts.entity;

import javax.validation.constraints.Size;

public class Post {
    public String fileName;
    @Size(min = 3, max = 255)
    public String title;
    @Size(min = 3)
    public String comment;

    public Post(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    public Post() {
    }
}
