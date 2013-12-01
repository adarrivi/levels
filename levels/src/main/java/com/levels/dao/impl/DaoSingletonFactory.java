package com.levels.dao.impl;

import com.levels.dao.LevelScoreDao;
import com.levels.dao.UserSessionDao;

/**
 * Singleton factory that grants access to the different in-memory Dao classes
 * 
 * @author adarrivi
 * 
 */
public class DaoSingletonFactory {

    // Defines the maximum sessions allowed in memory
    private static final int MAX_SESSIONS_ALLOWED = 10000;
    // Defines the maximum levels allowed in memory
    private static final int MAX_LEVELS_ALLOWED = 10000;
    // Defines the maximum high scores allowed per level
    private static final int MAX_SCORES_PER_LEVEL = 15;

    private static final DaoSingletonFactory INSTANCE = new DaoSingletonFactory();

    private UserSessionDaoInMemory USER_SESSION_DAO = new UserSessionDaoInMemory();
    private LevelScoreDaoInMemory LEVEL_SCORE_DAO = new LevelScoreDaoInMemory();

    public static DaoSingletonFactory getInstance() {
        return INSTANCE;
    }

    private DaoSingletonFactory() {
        setUpUserDao();
        setUpLevelScoreDao();
    }

    private void setUpUserDao() {
        USER_SESSION_DAO.setMaxUserSessionAllowed(MAX_SESSIONS_ALLOWED);
    }

    private void setUpLevelScoreDao() {
        LEVEL_SCORE_DAO.setMaxHighScoresPerLevel(MAX_SCORES_PER_LEVEL);
        LEVEL_SCORE_DAO.setMaxLevels(MAX_LEVELS_ALLOWED);

    }

    public UserSessionDao getUserSessionDao() {
        return USER_SESSION_DAO;
    }

    public LevelScoreDao getLevelScoreDao() {
        return LEVEL_SCORE_DAO;
    }
}
