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

package io.github.lasyard.web.jetty.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletExample extends HttpServlet {
    private static final long serialVersionUID = 4570203527331054198L;

    public static void main(String[] args) throws Exception {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setResourceBase(".");
        handler.addServlet(DefaultServlet.class, "/");

        Server server = new Server(8080);
        server.setHandler(handler);

        handler.addServlet(ServletExample.class, "/servlet");

        server.start();
        server.join();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body><h1>I am a servlet.</h1></body></html>");
    }
}
