package com.levels.dao.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.levels.dao.UserSessionDao;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;

/**
 * Singleton DAO class for the User's Sessions, keeping the results stored in
 * memory
 * 
 * @author adarrivi
 * 
 */
class UserSessionDaoInMemory implements UserSessionDao {

    private Map<Integer, UserSession> sessionMap = new ConcurrentHashMap<>();

    UserSessionDaoInMemory() {
        // Only Factory (and Unit tests) should have access to the
        // constructor
    }

    @Override
    public void saveOrUpdate(int userId, UserSession userSession) {
        sessionMap.put(userId, userSession);
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
