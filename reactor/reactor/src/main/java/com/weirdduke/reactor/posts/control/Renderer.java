package com.weirdduke.reactor.posts.control;

import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Renderer {

    private Source source;

    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    private MetricRegistry registry;

    @PostConstruct
    public void init() {
        try {
            source = Source.newBuilder("js", loadHandleBars(), "HandleBars").build();
        } catch (IOException ioe) {
            throw new IllegalStateException("Could not load handleBars", ioe);
        }
    }

    public String render(String templateContent, String postContent) {
        try {
            return renderInternal(templateContent, postContent);
        } catch (PolyglotException pe) {
            registry.counter("rendering_errors").inc();
            throw new IllegalStateException("Failed to render response " + pe.getMessage(), pe);
        }
    }

    public String renderInternal(String templateContent, String postContent) {
        try (Context jsContext = Context.create("js")) {
            jsContext.eval(source);
            var bindings = jsContext.getBindings("js");
            bindings.putMember("templateContent", templateContent);
            bindings.putMember("postContent", postContent);
            return jsContext.eval("js", getRenderLogic()).toString();
        }
    }

    private String getRenderLogic() {
        //language=JavaScript
        return """
                const postAsJson = JSON.parse(postContent)
                const compiledTemplate = Handlebars.compile(templateContent);
                compiledTemplate(postAsJson)
                """;
    }

    private Reader loadHandleBars() {
        var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("js/handlebars-v4.7.6.js");
        return new InputStreamReader(stream);
    }
}
