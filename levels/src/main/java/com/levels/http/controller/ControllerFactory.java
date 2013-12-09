package com.levels.http.controller;

import javax.annotation.concurrent.NotThreadSafe;

import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;

/**
 * Factory to grant access to all the different url controllers
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class ControllerFactory {

    public HttpStringResponseController createLoginController(LoginService loginService) {
        LoginController controller = new LoginController();
        controller.setLoginService(loginService);
        return controller;
    }

    public HttpStringResponseController createHighScoreController(LevelScoreService levelScoreService) {
        HighScoreController controller = new HighScoreController();
        controller.setLevelScoreService(levelScoreService);
        return controller;
    }

    public HttpStringResponseController createUserScoreController(LevelScoreService levelScoreService) {
        UserScoreController controller = new UserScoreController();
        controller.setLevelScoreService(levelScoreService);
        return controller;
    }
}
