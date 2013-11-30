package com.levels.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.levels.exception.InvalidParameterException;
import com.levels.exception.MaxMemoryReachedException;
import com.levels.http.controller.HttpStringController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpRequestRouterHandler implements HttpHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestRouterHandler.class);
    private List<HttpStringController> controllers = new ArrayList<>();

    void addController(HttpStringController controller) {
        controllers.add(controller);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            for (HttpStringController controller : controllers) {
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
            LOG.error("Invalid parameter found in the request", ex);
            writeResponse(exchange, "Invalid parameter: " + ex.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        } catch (MaxMemoryReachedException ex) {
            LOG.error("Maximum memory allocated limit reached: ", ex);
            writeResponse(exchange, "The maximum memory allocated limit has been reached; cannot process the request",
                    HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        LOG.error("Url not mapped: {}. Returning BadRequest", exchange.getRequestURI());
        writeResponse(exchange, "Unknown URL", HttpURLConnection.HTTP_BAD_REQUEST);
    }

    private boolean matchesRequestMethod(HttpExchange exchange, HttpStringController controller) {
        return controller.getRequestMethod().equals(exchange.getRequestMethod());
    }

    private boolean matchesUrl(HttpExchange exchange, HttpStringController controller) {
        Pattern regex = Pattern.compile(controller.getUrlRegexPattern());
        Matcher matcher = regex.matcher(exchange.getRequestURI().toString());
        return matcher.matches();
    }

    @SuppressWarnings("unchecked")
    private void writeResponseFromController(HttpExchange exchange, HttpStringController controller) {
        String response = controller.processRequest(exchange, (Map<String, Object>) exchange.getAttribute("parameters"));
        writeResponse(exchange, response, HttpURLConnection.HTTP_OK);
    }

    private void writeResponse(HttpExchange exchange, String content, int status) {
        try {
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(status, content.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException ex) {
            LOG.error("Exception while writing the response", ex);
            // Nothing else to do, just wait for other requests
        }
    }

}
