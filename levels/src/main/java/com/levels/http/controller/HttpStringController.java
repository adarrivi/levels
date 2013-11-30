package com.levels.http.controller;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public interface HttpStringController {

    String GET = "GET";
    String POST = "POST";

    String processRequest(HttpExchange exchange, Map<String, Object> parameters);

    String getUrlRegexPattern();

    String getRequestMethod();

}
