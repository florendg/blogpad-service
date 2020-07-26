package com.weirdduke.blogpad.posts.control;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class PostStoreException extends WebApplicationException {

    public PostStoreException(String message, Throwable cause) {
        super(Response.status(400)
                .header("message", message)
                .header("cause",cause.getMessage())
                .build());
    }
}
