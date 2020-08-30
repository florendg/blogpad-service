package com.weirdduke.blogpad.readiness.boundary;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
public class BlogPadReadiness implements HealthCheck {


    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("blogPad");
    }
}
