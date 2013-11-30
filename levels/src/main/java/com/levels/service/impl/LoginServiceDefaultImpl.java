package com.levels.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.levels.dao.UserSessionDao;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LoginService;

class LoginServiceDefaultImpl implements LoginService {

    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceDefaultImpl.class);

    private DateProvider dateProvider;
    private KeyGenerator keyGenerator;
    private UserSessionDao userSessionDao;

    LoginServiceDefaultImpl() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setUserSessionDao(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    @Override
    public String loginUser(int userId) {
        UserSession userSession = new UserSession(keyGenerator.generateUniqueKey(), dateProvider.getCurrentDate());
        userSessionDao.saveOrUpdate(userId, userSession);
        return userSession.getKey();
    }

    @Override
    public UserIdSessionDto getValidUserIdSessionByKey(String key) {
        UserIdSessionDto userSessionDto = userSessionDao.findUserSessionDtoBySessionKey(key);
        if (userSessionDto == null) {
            LOG.debug("No sessions found for the key {}", key);
            return null;
        }
        if (userSessionDto.hasExpired(dateProvider.getCurrentDate())) {
            LOG.debug("Session for user {} found, but has expired", userSessionDto.getUserId());
            return null;
        }
        return userSessionDto;
    }
}
