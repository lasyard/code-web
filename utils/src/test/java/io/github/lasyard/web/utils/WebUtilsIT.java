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

package io.github.lasyard.web.utils;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebUtilsIT extends JUnit4CitrusTestDesigner {
    @Test
    @CitrusTest
    public void testDownloadFromUrl() {
        async().actions(
            http().server("httpServer").receive()
                .get("/file/demo"),
            http().server("httpServer").send()
                .response(HttpStatus.OK)
                .payload("abc")
        );
        action(new AbstractTestAction() {
            @Override
            public void doExecute(TestContext testContext) {
                try {
                    byte[] bytes = WebUtils.downloadFromUrl(
                        new URL("http", "localhost", 8080, "/file/demo")
                    );
                    String msg = new String(bytes, StandardCharsets.UTF_8);
                    testContext.setVariable("msg", msg);
                    assertThat(msg, is("abc"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        traceVariables("msg");
        echo("Value of 'msg' is \"${msg}\".");
    }
}
