package com.weirdduke.reactor.posts.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReactorTest {

    private Renderer cut;

    @BeforeEach
    void setup() {
        cut = new Renderer();
        cut.init();
    }

    @Test
    void render() {

        assertEquals(
                //language=html
                """
                        <html>
                            <head>
                                <title>Hello Duke</title>
                            </head>
                            <body>
                                <h1>Hello Duke</h1>
                                <p>Welcome to the pleasure dome</p>
                            </body>
                        </html>
                        """, cut.render(
                        //language=html
                        """
                                <html>
                                    <head>
                                        <title>{{title}}</title>
                                    </head>
                                    <body>
                                        <h1>{{title}}</h1>
                                        <p>{{content}}</p>
                                    </body>
                                </html>
                                """,
                        //language=JSON
                        """
                                {"title":"Hello Duke",
                                "content":"Welcome to the pleasure dome"}
                                """));
    }
}