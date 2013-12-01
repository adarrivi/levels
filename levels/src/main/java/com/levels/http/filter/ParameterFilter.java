package com.levels.http.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.levels.http.controller.HttpStringController;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * This filter adds into HttpExchange a new tag with the request' url parameters
 * and post content (if any) parameters
 * 
 * @author adarrivi
 * 
 */
public class ParameterFilter extends Filter {

    public static final String PARAMETERS_TAG = "parameters";
    public static final String POST_BODY_TAG = "postBody";

    @Override
    public String description() {
        return "Retrieves the url GEt and POST parameters and includes them into HttpExchange";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        addUrlParameters(exchange);
        if (HttpStringController.POST.equalsIgnoreCase(exchange.getRequestMethod())) {
            addPostBody(exchange);
        }
        chain.doFilter(exchange);
    }

    private void addUrlParameters(HttpExchange exchange) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute(PARAMETERS_TAG, parameters);
    }

    private void addPostBody(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        exchange.setAttribute(POST_BODY_TAG, query);
    }

    private void parseQuery(String query, Map<String, String> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[?]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }
                parameters.put(key, value);
            }
        }
    }
}
