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

    private static final Logger LOG = LoggerFactory.getLogger(HttpHighScoreServer.class);

    private static final int HTTP_PORT = 8081;
    private static HttpServer server;

    private HttpHighScoreServer() {
        // This class should not to be instantiated
    }

    public static void main(String[] args) throws IOException {
        initializeServer();
        startServer();
    }

    private static void initializeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        HttpRequestRouterHandler requestRouter = createRequestRouter();
        HttpContext context = server.createContext("/", requestRouter);
        context.getFilters().add(new ParameterFilter());
    }

    private static HttpRequestRouterHandler createRequestRouter() {
        HttpRequestRouterHandler requestRouter = new HttpRequestRouterHandler();
        requestRouter.setParameterVerifier(new ParameterVerifier());
        requestRouter.addController(ControllerSingletonFactory.getInstance().getLoginController());
        requestRouter.addController(ControllerSingletonFactory.getInstance().getHighScoreController());
        requestRouter.addController(ControllerSingletonFactory.getInstance().getUserScoreController());
        return requestRouter;
    }

    private static void startServer() {
        server.start();
        LOG.debug("Server started listening on port {}", HTTP_PORT);
    }

    public static void stopServer() {
        server.stop(0);
        LOG.debug("Server stopped");
    }

}
