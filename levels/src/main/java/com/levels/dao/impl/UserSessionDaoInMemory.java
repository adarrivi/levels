package com.levels.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.ThreadSafe;

import com.levels.dao.UserSessionDao;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;
import com.levels.service.DateProvider;

/**
 * DAO class for the User's Sessions, keeping the results stored in memory. To
 * guarantee it thread-save, the class is final to avoid any modification by
 * extension
 * 
 * @author adarrivi
 * 
 */
@ThreadSafe
final class UserSessionDaoInMemory implements UserSessionDao {

    private DateProvider dateProvider;

    // Using concurrent map to have flexible iterators on it
    private ConcurrentHashMap<Integer, UserSession> sessionMap = new ConcurrentHashMap<>();

    UserSessionDaoInMemory() {
        // Only Factory (and Unit tests) should have access to the
        // constructor
    }

    void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    // Method to be used in unit tests
    int getSessionMapSize() {
        return sessionMap.size();
    }

    @Override
    public void saveOrUpdate(int userId, UserSession userSession) {
        // Moving alien methods outside of synchronised block
        List<Integer> expiredSessionsUserIds = getExpiredSessionsUserIds();
        // Sync required to keep data consistency (two non/atomic operations)
        synchronized (sessionMap) {
            removeExpiredSessions(expiredSessionsUserIds);
            sessionMap.put(userId, userSession);
        }
    }

    private List<Integer> getExpiredSessionsUserIds() {
        Date currentDate = dateProvider.getCurrentDate();
        List<Integer> expiredSessionsUserIds = new ArrayList<>();
        for (Entry<Integer, UserSession> sessionEntry : sessionMap.entrySet()) {
            UserSession userSession = sessionEntry.getValue();
            if (userSession.hasExpired(currentDate)) {
                expiredSessionsUserIds.add(sessionEntry.getKey());
            }
        }
        return expiredSessionsUserIds;
    }

    private void removeExpiredSessions(List<Integer> expiredSessionsUserIds) {
        for (Integer userId : expiredSessionsUserIds) {
            sessionMap.remove(userId);
        }
    }

    @Override
    public UserIdSessionDto findUserSessionDtoBySessionKey(String sessionKey) {
        // The iterator is a "weakly consistent" iterator that will never throw
        // ConcurrentException when the map is modified, so concurrent
        // executions along saveOrUpdate are thread-safe
        for (Entry<Integer, UserSession> sessionEntry : sessionMap.entrySet()) {
            if (sessionEntry.getValue().getKey().equals(sessionKey)) {
                return new UserIdSessionDto(sessionEntry.getKey(), sessionEntry.getValue());
            }
        }
        return null;
    }

}
