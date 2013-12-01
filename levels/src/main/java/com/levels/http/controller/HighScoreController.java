package com.levels.http.controller;

import java.util.Map;

import com.levels.service.LevelScoreService;

class HighScoreController implements HttpStringResponseController {

    private LevelScoreService levelScoreService;

    HighScoreController() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLevelScoreService(LevelScoreService levelScoreService) {
        this.levelScoreService = levelScoreService;
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/highscorelist";
    }

    @Override
    public String getRequestMethod() {
        return HttpStringResponseController.GET;
    }

    @Override
    public String processRequest(Map<String, String> urlParameters, Integer postBody, int integerFromUrl) {
        return levelScoreService.getHighScoreListPerLevel(integerFromUrl);
    }

}
