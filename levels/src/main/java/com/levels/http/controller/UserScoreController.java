package com.levels.http.controller;

import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

import com.levels.exception.InvalidParameterException;
import com.levels.service.LevelScoreService;

/**
 * Controller that processes user's score requests like
 * http://localhost:8081/2/score?sessionkey=UICSNDK (with the post body: 1500)
 * 
 * @author adarrivi
 * 
 */
@ThreadSafe
class UserScoreController implements HttpStringResponseController {

    private static final String SESSIONKEY_PARAMETER = "sessionkey";
    private static final String EMPTY = "";

    private LevelScoreService levelScoreService;

    UserScoreController() {
        // Only Factory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by Factory and Unit tests
    void setLevelScoreService(LevelScoreService levelScoreService) {
        this.levelScoreService = levelScoreService;
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/score.*";
    }

    @Override
    public String processRequest(Map<String, String> urlParameters, Integer postBody, int integerFromUrl) {
        verifyRequestParameters(urlParameters, postBody);
        levelScoreService.addScore(integerFromUrl, postBody, urlParameters.get(SESSIONKEY_PARAMETER));
        return EMPTY;
    }

    private void verifyRequestParameters(Map<String, String> urlParameters, Integer postBody) {
        if (urlParameters == null || urlParameters.isEmpty()) {
            throw new InvalidParameterException("No parameters found in the url.");
        }
        String sessionKey = urlParameters.get(SESSIONKEY_PARAMETER);
        if (sessionKey == null || EMPTY.equals(sessionKey)) {
            throw new InvalidParameterException("The parameter 'sessionkey' is mandatory and cannot be empty.");
        }
        if (postBody == null) {
            throw new InvalidParameterException("The user's score must be set in the POST body");
        }
    }

    @Override
    public String getRequestMethod() {
        return HttpStringResponseController.POST;
    }

}
