package com.weirdduke.reactor.posts.boundary;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("posts")
public class PostsResource {

    @Inject
    Reactor reactor;

    @GET
    @Traced
    @Bulkhead(2)
    @Fallback(fallbackMethod = "unavailableResponse")
    @ConcurrentGauge
    @Path("{title}")
    @Produces(MediaType.TEXT_HTML)
    public Response findPost(@PathParam("title") String title) {
        return Response.ok(this.reactor.render(title)).build();
    }

    public Response unavailableResponse(String title) {
        return Response.status(503)
                .header("reason", "server overloaded")
                .header("affected-post", title)
                .build();

    }

}

