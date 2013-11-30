package com.levels.http.controller;

import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.levels.service.LevelScoreService;
import com.sun.net.httpserver.HttpExchange;

class HighScoreController implements HttpStringController {

    private LevelScoreService levelScoreService;
    private ParameterVerifier parameterVerifier;

    HighScoreController() {
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
        return levelScoreService.getHighScoreListPerLevel(level);
    }

    private int getLevelFromURI(URI uri) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri.toString());
        matcher.find();
        return parameterVerifier.getValueAsUnsignedInt(matcher.group());
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/highscorelist";
    }

    @Override
    public String getRequestMethod() {
        return HttpStringController.GET;
    }

}
