package com.weirdduke.reactor.posts.boundary;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import com.weirdduke.reactor.posts.control.Renderer;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricRegistry.Type;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.weirdduke.reactor.posts.control.PostsResourceClient;

import java.text.MessageFormat;


public class Reactor {

    @Inject
    @RestClient
    PostsResourceClient client;

    @Inject
    Renderer renderer;

    @Inject
    @RegistryType(type = Type.APPLICATION)
    MetricRegistry registry;

    @Fallback(fallbackMethod = "getFallbackContent")
    public String render(String title) {
        Response response = this.client.findPost(title);
        var status = response.getStatus();
        registry.counter("content_find_post_status_" + status).inc();
        return renderer.render(getTemplate(), response.readEntity(String.class));
    }

    private String getTemplate() {
        //language=html
        return """
                <html lang="en">
                   <head>
                      <title>{{title}}</title>
                   </head>
                   <body>
                       <h1>{{title}}</h1>
                       <p>{{comment}}</p>
                   </body>
                </html>
                """;
    }

    public String getFallbackContent(String title) {
        return MessageFormat.format("""                             
                <html lang="en">               
                   <head>                      
                      <title>{0}</title> 
                   </head>                     
                   <body>                      
                       <h1>:-(</h1>      
                       <p>Error loading {1}</p>      
                   </body>                     
                </html>                        
                """, title, title);
    }

}