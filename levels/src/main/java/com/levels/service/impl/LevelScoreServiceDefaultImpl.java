package com.levels.service.impl;

import java.util.List;

import com.levels.dao.LevelScoreDao;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserScore;
import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;

/**
 * Singleton service to manage the score system
 * 
 * @author adarrivi
 * 
 */
class LevelScoreServiceDefaultImpl implements LevelScoreService {

    private static final String USER_SCORE_SEPPARATOR = "=";
    private static final String CSV_SEPPARATOR = ",";
    private LoginService loginService;
    private LevelScoreDao levelScoreDao;

    LevelScoreServiceDefaultImpl() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setLevelScoreDao(LevelScoreDao levelScoreDao) {
        this.levelScoreDao = levelScoreDao;
    }

    @Override
    public void addScore(int level, int score, String sessionKey) {
        UserIdSessionDto userIdSessionDto = loginService.getValidUserIdSessionByKey(sessionKey);
        levelScoreDao.addScoreIfBelongsToHallOfFame(level, userIdSessionDto.getUserId(), score);
    }

    @Override
    public String getHighScoreListPerLevel(int level) {
        List<UserScore> highScoreList = levelScoreDao.getHighScoreListPerLevel(level);
        return concatenateScoresIntoCsv(highScoreList);
    }

    private String concatenateScoresIntoCsv(List<UserScore> highScoreList) {
        StringBuilder builder = new StringBuilder();
        for (UserScore userScore : highScoreList) {
            if (builder.length() != 0) {
                builder.append(CSV_SEPPARATOR);
            }
            builder.append(userScore.getUserId()).append(USER_SCORE_SEPPARATOR).append(userScore.getScore());
        }
        return builder.toString();
    }

}
