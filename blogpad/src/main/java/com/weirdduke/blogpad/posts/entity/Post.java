package com.weirdduke.blogpad.posts.entity;

public class Post {
    public String title;
    public String comment;

    public Post(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    public Post() {
    }
}
