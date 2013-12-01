package com.levels.http.controller;

import com.levels.service.impl.ServiceSingletonFactory;

public class ControllerSingletonFactory {

    private static final ControllerSingletonFactory INSTANCE = new ControllerSingletonFactory();

    private LoginController LOGIN_CONTROLLER = new LoginController();
    private HighScoreController HIGH_SCORE_CONTROLLER = new HighScoreController();
    private UserScoreController USER_SCORE_CONTROLLER = new UserScoreController();
    private ParameterVerifier PARAMETER_VERIFIER = new ParameterVerifier();

    public static ControllerSingletonFactory getInstance() {
        return INSTANCE;
    }

    private ControllerSingletonFactory() {
        setUpLoginController();
        setUpHighScoreController();
        setUpUserScoreController();
    }

    private void setUpLoginController() {
        LOGIN_CONTROLLER.setLoginService(ServiceSingletonFactory.getInstance().getLoginService());
        LOGIN_CONTROLLER.setParameterVerifier(PARAMETER_VERIFIER);
    }

    private void setUpHighScoreController() {
        HIGH_SCORE_CONTROLLER.setLevelScoreService(ServiceSingletonFactory.getInstance().getLevelScoreService());
        HIGH_SCORE_CONTROLLER.setParameterVerifier(PARAMETER_VERIFIER);
    }

    private void setUpUserScoreController() {
        USER_SCORE_CONTROLLER.setLevelScoreService(ServiceSingletonFactory.getInstance().getLevelScoreService());
        USER_SCORE_CONTROLLER.setParameterVerifier(PARAMETER_VERIFIER);
    }

    public HttpStringController getLoginController() {
        return LOGIN_CONTROLLER;
    }

    public HttpStringController getHighScoreController() {
        return HIGH_SCORE_CONTROLLER;
    }

    public HttpStringController getUserScoreController() {
        return USER_SCORE_CONTROLLER;
    }
}
