package com.weirdduke.blogpad.posts.boundary;

import com.weirdduke.blogpad.posts.control.PostStore;
import com.weirdduke.blogpad.posts.entity.Post;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("posts")
public class PostResource {

    @Inject
    PostStore store;

    @Counted
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Context UriInfo uriInfo, Post post) {
        Post savedPost =  store.save(post);
        URI uri = uriInfo.getAbsolutePathBuilder().path(savedPost.fileName).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post findPost(@PathParam("title") String title) {
        return store.read(title);
    }

}
