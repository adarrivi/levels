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

import com.levels.http.controller.HttpStringResponseController;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * This filter adds into HttpExchange a new tag with the request's url
 * parameters and post content (if any)
 * 
 * @author adarrivi
 * 
 */
public class ParameterFilter extends Filter {

    public static final String URL_PARAMETERS_TAG = "parameters";
    public static final String POST_BODY_TAG = "postBody";

    private static final String PARAM_VALUE_SEPARATOR_REGEX = "[=]";
    private static final String URL_PARAMETER_SEPARATOR_REGEX = "[?]";
    private static final String FILE_ENCODING = System.getProperty("file.encoding");

    @Override
    public String description() {
        return "Retrieves the url and post parameters and adds them back into HttpExchange";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        addUrlParameters(exchange);
        if (HttpStringResponseController.POST.equalsIgnoreCase(exchange.getRequestMethod())) {
            addPostBody(exchange);
        }
        chain.doFilter(exchange);
    }

    private void addUrlParameters(HttpExchange exchange) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute(URL_PARAMETERS_TAG, parameters);
    }

    private void addPostBody(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        exchange.setAttribute(POST_BODY_TAG, query);
    }

    private void parseQuery(String query, Map<String, String> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split(URL_PARAMETER_SEPARATOR_REGEX);
            for (String pair : pairs) {
                String param[] = pair.split(PARAM_VALUE_SEPARATOR_REGEX);
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], FILE_ENCODING);
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], FILE_ENCODING);
                }
                parameters.put(key, value);
            }
        }
    }
}
