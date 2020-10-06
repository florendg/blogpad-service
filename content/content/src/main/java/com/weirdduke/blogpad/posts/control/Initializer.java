package com.weirdduke.blogpad.posts.control;

import com.weirdduke.blogpad.posts.entity.Post;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;


@Singleton
@Startup
public class Initializer {

    private static final String TITLE = "initial";

    @Inject
    private PostStore store;

    @PostConstruct
    public void initialFirstPost() {
        if (postExists()) {
            return;
        }
        store.createNew(createPost());
    }

    @Produces
    @Liveness
    public HealthCheck initialExists() {
        return () -> HealthCheckResponse.named("initial-post-exists")
                .state(postExists())
                .build();
    }

    private Post fetchPost() {
        return store.read(TITLE);
    }

    private boolean postExists() {
        Post post = fetchPost();
        if(post == null) {
            return false;
        }
        return TITLE.equalsIgnoreCase(post.title);
    }

    private Post createPost() {
        Post post = new Post();
        post.title = TITLE;
        post.comment = "This is the initial post";
        return post;
    }


}
