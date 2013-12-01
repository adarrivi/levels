package com.levels.http.controller;

import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.levels.http.filter.ParameterFilter;
import com.levels.service.LevelScoreService;
import com.sun.net.httpserver.HttpExchange;

class UserScoreController implements HttpStringController {

    private LevelScoreService levelScoreService;
    private ParameterVerifier parameterVerifier;

    UserScoreController() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setParameterVerifier(ParameterVerifier parameterVerifier) {
        this.parameterVerifier = parameterVerifier;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLevelScoreService(LevelScoreService levelScoreService) {
        this.levelScoreService = levelScoreService;
    }

    @Override
    public String processRequest(HttpExchange exchange, Map<String, Object> parameters) {
        int level = getLevelFromURI(exchange.getRequestURI());
        String scoreValue = (String) exchange.getAttribute(ParameterFilter.POST_BODY_TAG);
        levelScoreService.addScore(level, Integer.valueOf(scoreValue), (String) parameters.get("sessionkey"));
        return "";
    }

    private int getLevelFromURI(URI uri) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri.toString());
        matcher.find();
        return parameterVerifier.getValueAsUnsignedInt(matcher.group());
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/score.*";
    }

    @Override
    public String getRequestMethod() {
        return HttpStringController.POST;
    }

}
