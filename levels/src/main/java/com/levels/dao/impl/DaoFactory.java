package com.levels.dao.impl;

import javax.annotation.concurrent.NotThreadSafe;

import com.levels.dao.LevelScoreDao;
import com.levels.dao.UserSessionDao;
import com.levels.service.DateProvider;

/**
 * Dao factory
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class DaoFactory {

    // Defines the maximum high scores allowed per level
    private static final int MAX_SCORES_PER_LEVEL = 15;

    public UserSessionDao createUserSessionDao(DateProvider dateProvider) {
        UserSessionDaoInMemory userSessionDaoInMemory = new UserSessionDaoInMemory();
        userSessionDaoInMemory.setDateProvider(dateProvider);
        return userSessionDaoInMemory;
    }

    public LevelScoreDao createLevelScoreDao() {
        LevelScoreDaoInMemory levelScoreDaoInMemory = new LevelScoreDaoInMemory();
        levelScoreDaoInMemory.setMaxHighScoresPerLevel(MAX_SCORES_PER_LEVEL);
        return levelScoreDaoInMemory;
    }
}
