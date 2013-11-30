package com.levels.dao.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.levels.dao.UserSessionDao;
import com.levels.exception.MaxMemoryReachedException;
import com.levels.model.UserIdSessionDto;
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
            throw new MaxMemoryReachedException("Maximum sessions in memory reached: " + maxUserSessionAllowed);
        }
    }

    @Override
    public UserIdSessionDto findUserSessionDtoBySessionKey(String sessionKey) {
        // UserId and Session is a One to One relationship, so it's safe return
        // the first key coincidence.
        for (Entry<Integer, UserSession> sessionEntry : sessionMap.entrySet()) {
            if (sessionEntry.getValue().getKey().equals(sessionKey)) {
                return new UserIdSessionDto(sessionEntry.getKey(), sessionEntry.getValue());
            }
        }
        return null;
    }

}
