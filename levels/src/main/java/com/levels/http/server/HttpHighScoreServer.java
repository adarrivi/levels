package com.levels.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.levels.dao.LevelScoreDao;
import com.levels.dao.UserSessionDao;
import com.levels.dao.impl.DaoFactory;
import com.levels.http.controller.ControllerFactory;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;
import com.levels.service.impl.ServiceFactory;
import com.sun.net.httpserver.HttpServer;

/**
 * Main Server class. Starts a Http server listening requests.
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
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
        // Adding multi-thread pool
        server.setExecutor(Executors.newCachedThreadPool());
        HttpRequestRouterHandler requestRouter = createRequestRouter();
        server.createContext("/", requestRouter);
    }

    private static HttpRequestRouterHandler createRequestRouter() {
        HttpRequestRouterHandler requestRouter = new HttpRequestRouterHandler();

        DateProvider dateProvider = new DateProvider();
        DaoFactory daoFactory = new DaoFactory();
        LevelScoreDao levelScoreDao = daoFactory.createLevelScoreDao();
        UserSessionDao userSessionDao = daoFactory.createUserSessionDao(dateProvider);

        ServiceFactory serviceFactory = new ServiceFactory();
        KeyGenerator keyGenerator = serviceFactory.createKeyGenerator();
        LoginService loginService = serviceFactory.createLoginService(userSessionDao, dateProvider, keyGenerator);
        LevelScoreService levelScoreService = serviceFactory.createLevelScoreService(loginService, levelScoreDao);

        ControllerFactory controllerFactory = new ControllerFactory();
        requestRouter.addController(controllerFactory.createLoginController(loginService));
        requestRouter.addController(controllerFactory.createHighScoreController(levelScoreService));
        requestRouter.addController(controllerFactory.createUserScoreController(levelScoreService));

        ParameterVerifier parameterVerifier = new ParameterVerifier();
        HttpExchangeParameterHelper httpExchangeParameterHelper = new HttpExchangeParameterHelper();
        httpExchangeParameterHelper.setParameterVerifier(parameterVerifier);

        requestRouter.setHttpExchangeParameterHelper(httpExchangeParameterHelper);

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
