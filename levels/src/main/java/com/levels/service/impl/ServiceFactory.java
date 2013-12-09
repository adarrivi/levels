package com.levels.service.impl;

import javax.annotation.concurrent.NotThreadSafe;

import com.levels.dao.LevelScoreDao;
import com.levels.dao.UserSessionDao;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;

/**
 * Factory to grant access to the different services implementations
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class ServiceFactory {

    public LoginService createLoginService(UserSessionDao userSessionDao, DateProvider dateProvider, KeyGenerator keyGenerator) {
        LoginServiceDefaultImpl loginServiceDefaultImpl = new LoginServiceDefaultImpl();
        loginServiceDefaultImpl.setDateProvider(dateProvider);
        loginServiceDefaultImpl.setKeyGenerator(keyGenerator);
        loginServiceDefaultImpl.setUserSessionDao(userSessionDao);
        return loginServiceDefaultImpl;
    }

    public LevelScoreService createLevelScoreService(LoginService loginService, LevelScoreDao levelScoreDao) {
        LevelScoreServiceDefaultImpl levelScoreServiceDefaultImpl = new LevelScoreServiceDefaultImpl();
        levelScoreServiceDefaultImpl.setLevelScoreDao(levelScoreDao);
        levelScoreServiceDefaultImpl.setLoginService(loginService);
        return levelScoreServiceDefaultImpl;
    }

    public KeyGenerator createKeyGenerator() {
        return new KeyGeneratorReasonablyUnique();
    }

}
