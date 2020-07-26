package com.weirdduke.blogpad.posts.boundary;

import com.weirdduke.blogpad.posts.control.PostStore;
import com.weirdduke.blogpad.posts.entity.Post;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("posts")
public class PostResource {

    @Inject
    PostStore postStore;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(Post post) {
        postStore.save(post);
    }
}
