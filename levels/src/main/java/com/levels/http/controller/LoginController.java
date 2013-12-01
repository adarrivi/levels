package com.levels.http.controller;

import java.util.Map;

import com.levels.service.LoginService;

/**
 * Controller that processes login requests like
 * http://localhost:8081/4711/login --> UICSNDK
 * 
 * @author adarrivi
 * 
 */
class LoginController implements HttpStringResponseController {

    private LoginService loginService;

    LoginController() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/login";
    }

    @Override
    public String getRequestMethod() {
        return HttpStringResponseController.GET;
    }

    @Override
    public String processRequest(Map<String, String> urlParameters, Integer postBody, int integerFromUrl) {
        return loginService.loginUser(integerFromUrl);
    }

}
