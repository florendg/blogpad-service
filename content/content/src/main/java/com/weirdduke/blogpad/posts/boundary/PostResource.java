package com.weirdduke.blogpad.posts.boundary;

import com.weirdduke.blogpad.posts.control.PostStore;
import com.weirdduke.blogpad.posts.entity.Post;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.validation.Valid;
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


    @POST
    @APIResponse(
        responseCode = "400",
        description = "Post with given title already exists. Use PUT for update"
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@Context UriInfo uriInfo,@Valid Post post) {
        Post savedPost =  store.createNew(post);
        URI uri = uriInfo.getAbsolutePathBuilder().path(savedPost.fileName).build();
        return Response.created(uri).build();
    }

    @Counted
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Context UriInfo uriInfo,@Valid Post post) {
        store.update(post);
        return Response.ok().build();
    }

    @Timed
    @GET
    @Traced
    @Path("{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post findPost(@PathParam("title") String title) {
        return store.read(title);
    }

}
