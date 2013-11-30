package com.levels.dao.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.levels.dao.UserSessionDao;
import com.levels.exception.MaxSessionsReachedException;
import com.levels.model.UserSession;

class UserSessionDaoInMemory implements UserSessionDao {

    private Map<Integer, UserSession> sessionMap = new ConcurrentHashMap<>();

    private int maxUserSessionAllowed;

    UserSessionDaoInMemory() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setMaxUserSessionAllowed(int maxUserSessionAllowed) {
        this.maxUserSessionAllowed = maxUserSessionAllowed;
    }

    @Override
    public void saveOrUpdate(int userId, UserSession userSession) {
        assertSpaceEnoughInSessionMap();
        sessionMap.put(userId, userSession);
    }

    private void assertSpaceEnoughInSessionMap() {
        if (sessionMap.size() >= maxUserSessionAllowed) {
            throw new MaxSessionsReachedException("Maximum sessions in memory reached: " + maxUserSessionAllowed);
        }
    }

    @Override
    public void deleteUserSession(int userId) {
        sessionMap.remove(userId);
    }

    @Override
    public UserSession findUserSession(int userId) {
        return sessionMap.get(userId);
    }

}
