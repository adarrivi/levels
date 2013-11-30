package com.levels.dao.impl;

import com.levels.dao.UserSessionDao;

public class DaoSingletonFactory {

    private static final int MAX_SESSIONS_ALLOWED = 3000;
    private static final UserSessionDaoInMemory USER_SESSION_DAO_IN_MEMORY = new UserSessionDaoInMemory();

    private static final DaoSingletonFactory INSTANCE = new DaoSingletonFactory();

    private DaoSingletonFactory() {
        USER_SESSION_DAO_IN_MEMORY.setMaxUserSessionAllowed(MAX_SESSIONS_ALLOWED);
    }

    public static DaoSingletonFactory getInstance() {
        return INSTANCE;
    }

    public UserSessionDao getUserSessionDaoInMemory() {
        return USER_SESSION_DAO_IN_MEMORY;
    }
}
