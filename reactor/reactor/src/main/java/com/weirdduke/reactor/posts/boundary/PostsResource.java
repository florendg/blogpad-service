package com.weirdduke.reactor.posts.boundary;

import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("posts")
public class PostsResource {

    @Inject
    Reactor reactor;

    @GET
    @Traced
    @Path("{title}")
    @Produces(MediaType.TEXT_HTML)
    public String findPost(@PathParam("title") String title){
        return this.reactor.render(title);
    }

}

