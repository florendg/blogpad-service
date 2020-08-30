package com.weirdduke.service.posts.boundary;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("posts")
public interface PostResourceClient {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updatePost(JsonObject post);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response createNew(JsonObject post);

    @GET
    @Path("{title}")
    @Produces(MediaType.APPLICATION_JSON)
    Response findPost(@PathParam("title") String title);
}
