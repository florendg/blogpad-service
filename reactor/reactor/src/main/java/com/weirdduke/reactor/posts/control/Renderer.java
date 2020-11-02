package com.weirdduke.reactor.posts.control;

import org.graalvm.polyglot.Context;

public class Renderer {

    String render(String template, String content) {
        try (Context jsContext = Context.create("js")) {
            var bindings = jsContext.getBindings("js");
            bindings.putMember("message", "Duke");
            return jsContext.eval("js", "message + 42").toString();
        }
    }
}
