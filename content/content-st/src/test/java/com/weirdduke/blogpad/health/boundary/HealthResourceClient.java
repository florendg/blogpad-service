package com.weirdduke.blogpad.health.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("health")
public interface HealthResourceClient {

    @GET
    @Path("live")
    Response liveness();

    @GET
    @Path("ready")
    Response readiness();
}
