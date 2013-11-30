package com.levels.http.controller;

import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.levels.service.LoginService;
import com.sun.net.httpserver.HttpExchange;

class LoginController implements HttpStringController {

    private LoginService loginService;
    private ParameterVerifier parameterVerifier;

    LoginController() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setParameterVerifier(ParameterVerifier parameterVerifier) {
        this.parameterVerifier = parameterVerifier;
    }

    @Override
    public String processRequest(HttpExchange exchange, Map<String, Object> parameters) {
        int userId = getUserIdFromURI(exchange.getRequestURI());
        return loginService.loginUser(userId);
    }

    private int getUserIdFromURI(URI uri) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri.toString());
        matcher.find();
        return parameterVerifier.getValueAsUnsignedInt(matcher.group());
    }

    @Override
    public String getUrlRegexPattern() {
        return "/\\d+/login";
    }

    @Override
    public String getRequestMethod() {
        return HttpStringController.GET;
    }

}
