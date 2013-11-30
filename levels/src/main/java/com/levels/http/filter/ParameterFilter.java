package com.levels.http.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * This filter adds into HttpExchange a new tag with the request' GET and POST
 * parameters
 * 
 * @author adarrivi
 * 
 */
public class ParameterFilter extends Filter {

    private static final String PARAMETERS_TAG = "parameters";

    @Override
    public String description() {
        return "Retrieves the url GEt and POST parameters and includes them into HttpExchange";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        parseGetParameters(exchange);
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            parsePostParameters(exchange);
        }
        chain.doFilter(exchange);
    }

    private void parseGetParameters(HttpExchange exchange) throws UnsupportedEncodingException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute(PARAMETERS_TAG, parameters);
    }

    @SuppressWarnings("unchecked")
    private void parsePostParameters(HttpExchange exchange) throws IOException {
        Map<String, Object> parameters = (Map<String, Object>) exchange.getAttribute(PARAMETERS_TAG);
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);
    }

    @SuppressWarnings("unchecked")
    private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[&]");
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

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
