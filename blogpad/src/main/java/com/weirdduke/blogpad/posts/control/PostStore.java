package com.weirdduke.blogpad.posts.control;

import com.weirdduke.blogpad.posts.entity.Post;

import javax.json.bind.Jsonb;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static javax.json.bind.JsonbBuilder.create;

public class PostStore {

    void write(final @NotNull String fileName, final @NotNull String content) throws IOException {
        Path path = Path.of(fileName);
        Files.writeString(path, content);
    }

    public String serialize(final @NotNull Post post) {
        Jsonb jsonb = create();
        return jsonb.toJson(post);
    }

}
