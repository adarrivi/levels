package com.levels.service.impl;

import com.levels.dao.impl.DaoSingletonFactory;
import com.levels.service.DateProvider;
import com.levels.service.LoginService;

public class ServiceSingletonFactory {

    private static final ServiceSingletonFactory INSTANCE = new ServiceSingletonFactory();

    private KeyGeneratorReasonablyUnique KEY_GENERATOR = new KeyGeneratorReasonablyUnique();
    private LoginServiceDefaultImpl LOGIN_SERVICE = new LoginServiceDefaultImpl();

    public static ServiceSingletonFactory getInstance() {
        return INSTANCE;
    }

    private ServiceSingletonFactory() {
        setUpLoginService();
    }

    private void setUpLoginService() {
        LOGIN_SERVICE.setDateProvider(DateProvider.getInstance());
        LOGIN_SERVICE.setKeyGenerator(KEY_GENERATOR);
        LOGIN_SERVICE.setUserSessionDao(DaoSingletonFactory.getInstance().getUserSessionDao());
    }

    public LoginService getLoginService() {
        return LOGIN_SERVICE;
    }

}
