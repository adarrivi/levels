package com.levels.http.controller;

import java.util.Map;

public interface HttpStringResponseController {

    String GET = "GET";
    String POST = "POST";

    String processRequest(Map<String, String> urlParameters, Integer postBody, int integerFromUrl);

    String getUrlRegexPattern();

    String getRequestMethod();

}
