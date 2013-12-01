package com.levels.http.controller;

import com.levels.service.impl.ServiceSingletonFactory;

public class ControllerSingletonFactory {

    private static final ControllerSingletonFactory INSTANCE = new ControllerSingletonFactory();

    private LoginController LOGIN_CONTROLLER = new LoginController();
    private HighScoreController HIGH_SCORE_CONTROLLER = new HighScoreController();
    private UserScoreController USER_SCORE_CONTROLLER = new UserScoreController();

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
    }

    private void setUpHighScoreController() {
        HIGH_SCORE_CONTROLLER.setLevelScoreService(ServiceSingletonFactory.getInstance().getLevelScoreService());
    }

    private void setUpUserScoreController() {
        USER_SCORE_CONTROLLER.setLevelScoreService(ServiceSingletonFactory.getInstance().getLevelScoreService());
    }

    public HttpStringResponseController getLoginController() {
        return LOGIN_CONTROLLER;
    }

    public HttpStringResponseController getHighScoreController() {
        return HIGH_SCORE_CONTROLLER;
    }

    public HttpStringResponseController getUserScoreController() {
        return USER_SCORE_CONTROLLER;
    }
}
