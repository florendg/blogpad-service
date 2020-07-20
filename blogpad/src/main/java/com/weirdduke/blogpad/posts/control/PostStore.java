package com.weirdduke.blogpad.posts.control;

import com.weirdduke.blogpad.posts.entity.Post;

import javax.json.bind.Jsonb;
import javax.validation.constraints.NotNull;

import static javax.json.bind.JsonbBuilder.create;

public class PostStore {

    public String serialize(final @NotNull Post post) {
        Jsonb jsonb = create();
        return jsonb.toJson(post);
    }

}
