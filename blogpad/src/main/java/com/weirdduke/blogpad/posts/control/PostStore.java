package com.weirdduke.blogpad.posts.control;

import com.weirdduke.blogpad.posts.entity.Post;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static javax.json.bind.JsonbBuilder.create;

public class PostStore {

    @Inject
    @ConfigProperty(name = "root.storage.dir")
    String storageDir;
    Path storageDirectoryPath;

    @PostConstruct
    void init() {
        this.storageDirectoryPath = Path.of(storageDir);
    }

    public Post create(final @NotNull Post post) {
        try {
            var stringified = serialize(post);
            var fileName = normalize(post.title);
            if(fileExists(fileName)) {
                throw new PostStoreException("Post named " + fileName + "already exists");
            }
            post.fileName = fileName;
            write(fileName, stringified);
            return post;
        } catch (Exception exception) {
            throw new PostStoreException("Failed to save " + post.title, exception);
        }
    }

    public Post read(final @NotNull String title) {
        try {
            var normalizedTitle = normalize(title);
            var stringified = readString(normalizedTitle);
            return deserialize(stringified);
        } catch (Exception exception) {
            throw new PostStoreException("Failed to find " + title + ". ", exception);
        }
    }

    public void update(Post post) {
        try {
            var stringified = serialize(post);
            var fileName = normalize(post.title);
            post.fileName = fileName;
            write(fileName, stringified);
        } catch (Exception exception) {
            throw new PostStoreException("Failed to save " + post.title, exception);
        }
    }

    boolean fileExists(String fileName) {
        Path pathToPost = storageDirectoryPath.resolve(fileName);
        return Files.exists(pathToPost);
    }

    void write(final @NotNull String fileName, final @NotNull String content) throws IOException {
        var path = this.storageDirectoryPath.resolve(fileName);
        Files.writeString(path, content);
    }

    String readString(final @NotNull String fileName) throws IOException {
        var path = this.storageDirectoryPath.resolve(fileName);
        return Files.readString(path);
    }

    String serialize(final @NotNull Post post) throws Exception {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(post);
        }
    }

    Post deserialize(final @NotNull String stringified) throws Exception {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(stringified, Post.class);
        }
    }

    private String normalize(String title) {
        return title.codePoints().map(this::convert)
                .collect(StringBuffer::new, StringBuffer::appendCodePoint, StringBuffer::append).toString();
    }

    private int convert(int codePoint) {
        if (Character.isLetterOrDigit(codePoint)) {
            return codePoint;
        } else {
            return "-".codePoints().findFirst().orElseThrow();
        }
    }
}
