package com.levels.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.levels.exception.InvalidParameterException;
import com.levels.http.controller.HttpStringResponseController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Routes all the incoming requests to the different controllers depending on
 * the url
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
class HttpRequestRouterHandler implements HttpHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestRouterHandler.class);

    private List<HttpStringResponseController> controllers = new ArrayList<>();

    private HttpExchangeParameterHelper httpExchangeParameterHelper;

    HttpRequestRouterHandler() {
        // Limiting scope, so it can be used only within server package
    }

    // Limiting scope, so it can be used only within server package
    void addController(HttpStringResponseController controller) {
        controllers.add(controller);
    }

    // Limiting scope, so it can be used only within server package
    void setHttpExchangeParameterHelper(HttpExchangeParameterHelper httpExchangeParameterHelper) {
        this.httpExchangeParameterHelper = httpExchangeParameterHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            for (HttpStringResponseController controller : controllers) {
                if (matchesRequestMethod(exchange, controller) && matchesUrl(exchange, controller)) {
                    long timeToProcessNs = System.nanoTime();
                    writeResponseFromController(exchange, controller);
                    timeToProcessNs = System.nanoTime() - timeToProcessNs;
                    LOG.debug("It took {}ms to process the '{}' request and build the response.",
                            TimeUnit.NANOSECONDS.toMillis(timeToProcessNs), exchange.getRequestURI());
                    return;

                }
            }
        } catch (InvalidParameterException ex) {
            LOG.debug("Invalid parameter found in the request", ex);
            writeResponse(exchange, "Invalid parameter: " + ex.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        LOG.error("{} Url not mapped: {}. Returning BadRequest", exchange.getRequestMethod(), exchange.getRequestURI());
        writeResponse(exchange, "Unknown URL", HttpURLConnection.HTTP_BAD_REQUEST);
    }

    private boolean matchesRequestMethod(HttpExchange exchange, HttpStringResponseController controller) {
        return controller.getRequestMethod().equals(exchange.getRequestMethod());
    }

    private boolean matchesUrl(HttpExchange exchange, HttpStringResponseController controller) {
        Pattern regex = Pattern.compile(controller.getUrlRegexPattern());
        Matcher matcher = regex.matcher(exchange.getRequestURI().toString());
        return matcher.matches();
    }

    private void writeResponseFromController(HttpExchange exchange, HttpStringResponseController controller) {
        RequestParameter requestParameters = httpExchangeParameterHelper.retrieveParameters(exchange);
        String response = controller.processRequest(requestParameters.getUrlParameters(), requestParameters.getPostBody(),
                requestParameters.getIntegerFromUrl());
        writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
    }

    private void writeResponse(HttpExchange exchange, String content, int status) {
        try {
            Headers header = exchange.getResponseHeaders();
            header.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(status, content.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException ex) {
            // Nothing else we can do, just wait for other requests
            LOG.error("Exception while writing the response", ex);
        }
    }

}
