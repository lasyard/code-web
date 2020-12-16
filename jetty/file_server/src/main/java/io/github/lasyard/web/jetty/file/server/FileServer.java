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

package io.github.lasyard.web.jetty.file.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

public final class FileServer {
    private FileServer() {
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setResourceBase(".");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
