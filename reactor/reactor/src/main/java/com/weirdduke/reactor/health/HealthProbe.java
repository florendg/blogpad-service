package com.weirdduke.reactor.health;

import com.weirdduke.reactor.posts.control.PostsResourceClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Liveness
@Readiness
@ApplicationScoped
public class HealthProbe implements HealthCheck {

    @Inject
    @RestClient
    private PostsResourceClient client;

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("content-available").state(checkDefaultPost()).build();
    }

    private boolean checkDefaultPost() {
        try {
            Response response = client.findPost("initial");
            return response.getStatus() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
