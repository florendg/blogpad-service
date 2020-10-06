package com.weirdduke.blogpad.posts.entity;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Post {
    @Schema(readOnly = true)
    public String fileName;
    @Schema(required = true,example = "Hello OpenAPI 3")
    @Size(min = 4, max = 255)
    public String title;
    @Schema(required = true, example = "This is a example Document")
    @Size(min = 3)
    public String comment;

    @Schema(readOnly = true, type = SchemaType.STRING, format = "date-time")
    public LocalDateTime createdAt;
    @Schema(readOnly = true, type = SchemaType.STRING, format = "date-time")
    public LocalDateTime modifiedAt;

    public Post(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }

    public Post() {
    }

    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    public void setModifiedAt() {
        modifiedAt = LocalDateTime.now();
    }
}
