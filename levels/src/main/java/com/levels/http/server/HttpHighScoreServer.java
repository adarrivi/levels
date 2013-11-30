package com.levels.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.levels.http.controller.ControllerSingletonFactory;
import com.levels.http.filter.ParameterFilter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class HttpHighScoreServer {

    private static final int HTTP_PORT = 8081;
    private static final Logger LOG = LoggerFactory.getLogger(HttpHighScoreServer.class);
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        HttpRequestRouterHandler requestRouter = new HttpRequestRouterHandler();
        requestRouter.addController(ControllerSingletonFactory.getInstance().getLoginController());
        HttpContext context = server.createContext("/", requestRouter);
        context.getFilters().add(new ParameterFilter());
        server.start();
        LOG.debug("Server started listening on port {}", HTTP_PORT);
    }

    public static void stopServer() {
        server.stop(0);
        LOG.debug("Server stopped");
    }

}
