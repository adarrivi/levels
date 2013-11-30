package com.levels.http.controller;

import com.levels.service.impl.ServiceSingletonFactory;

public class ControllerSingletonFactory {

    private static final ControllerSingletonFactory INSTANCE = new ControllerSingletonFactory();

    private LoginController LOGIN_CONTROLLER = new LoginController();
    private ParameterVerifier PARAMETER_VERIFIER = new ParameterVerifier();

    public static ControllerSingletonFactory getInstance() {
        return INSTANCE;
    }

    private ControllerSingletonFactory() {
        setUpLoginController();
    }

    private void setUpLoginController() {
        LOGIN_CONTROLLER.setLoginService(ServiceSingletonFactory.getInstance().getLoginService());
        LOGIN_CONTROLLER.setParameterVerifier(PARAMETER_VERIFIER);
    }

    public HttpStringController getLoginController() {
        return LOGIN_CONTROLLER;
    }

}
