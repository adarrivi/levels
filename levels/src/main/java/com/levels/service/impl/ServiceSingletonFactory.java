package com.levels.service.impl;

import com.levels.dao.impl.DaoSingletonFactory;
import com.levels.service.DateProvider;
import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;

public class ServiceSingletonFactory {

    private static final ServiceSingletonFactory INSTANCE = new ServiceSingletonFactory();

    private KeyGeneratorReasonablyUnique KEY_GENERATOR = new KeyGeneratorReasonablyUnique();
    private LoginServiceDefaultImpl LOGIN_SERVICE = new LoginServiceDefaultImpl();
    private LevelScoreServiceDefaultImpl LEVEL_SERVICE = new LevelScoreServiceDefaultImpl();

    public static ServiceSingletonFactory getInstance() {
        return INSTANCE;
    }

    private ServiceSingletonFactory() {
        setUpLoginService();
        setUpLevelService();
    }

    private void setUpLoginService() {
        LOGIN_SERVICE.setDateProvider(DateProvider.getInstance());
        LOGIN_SERVICE.setKeyGenerator(KEY_GENERATOR);
        LOGIN_SERVICE.setUserSessionDao(DaoSingletonFactory.getInstance().getUserSessionDao());
    }

    private void setUpLevelService() {
        LEVEL_SERVICE.setLevelScoreDao(DaoSingletonFactory.getInstance().getLevelScoreDao());
        LEVEL_SERVICE.setLoginService(LOGIN_SERVICE);
    }

    public LoginService getLoginService() {
        return LOGIN_SERVICE;
    }

    public LevelScoreService getLevelScoreService() {
        return LEVEL_SERVICE;
    }

}
