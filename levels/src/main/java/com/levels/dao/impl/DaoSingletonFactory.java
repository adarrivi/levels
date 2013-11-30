package com.levels.dao.impl;

import com.levels.dao.UserSessionDao;

public class DaoSingletonFactory {

    private static final int MAX_SESSIONS_ALLOWED = 10000;
    private static final DaoSingletonFactory INSTANCE = new DaoSingletonFactory();

    private UserSessionDaoInMemory USER_SESSION_DAO = new UserSessionDaoInMemory();

    public static DaoSingletonFactory getInstance() {
        return INSTANCE;
    }

    private DaoSingletonFactory() {
        USER_SESSION_DAO.setMaxUserSessionAllowed(MAX_SESSIONS_ALLOWED);
    }

    public UserSessionDao getUserSessionDao() {
        return USER_SESSION_DAO;
    }
}
