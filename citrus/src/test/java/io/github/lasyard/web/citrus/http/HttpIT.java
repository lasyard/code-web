/*
 * Copyright 2020 lasyard@github.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lasyard.web.citrus.http;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class HttpIT extends JUnit4CitrusTestDesigner {
    @Test
    @CitrusTest
    public void testHttpServerClientRaw() {
        description("Using raw send/receive.");
        async().actions(
            receive("httpServer")
                .header("Accept", "text/html")
                .payload(""),
            send("httpServer")
                .header("ContentType", "text/html")
                .payload("<html></html>")
        );
        send("httpClient")
            .header("Accept", "text/html")
            .payload("");
        receive("httpClient")
            .header("ContentType", "text/html")
            .payload("<html></html>");
    }

    @Test
    @CitrusTest
    public void testHttpServerClient() {
        description("Using http send/receive.");
        async().actions(
            http().server("httpServer").receive()
                .get()
                .accept("text/html"),
            http().server("httpServer").send()
                .response(HttpStatus.OK)
                .version("HTTP/1.1")
                .contentType("text/html")
                .payload("<html></html>")
        );
        http().client("httpClient").send()
            .get()
            .accept("text/html");
        http().client(("httpClient")).receive()
            .response(HttpStatus.OK)
            .version("HTTP/1.1")
            .contentType("text/html")
            .payload("<html></html>");
    }
}
