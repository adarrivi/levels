package com.levels.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.levels.dao.LevelScoreDao;
import com.levels.exception.MaxMemoryReachedException;
import com.levels.model.LevelHighScores;
import com.levels.model.UserScore;

class LevelScoreDaoInMemory implements LevelScoreDao {

    private int maxHighScoresPerLevel;
    private int maxLevels;
    private Map<Integer, LevelHighScores> highScoresMap = new ConcurrentHashMap<>();

    LevelScoreDaoInMemory() {
        // Only SingletonFactory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setMaxHighScoresPerLevel(int maxHighScoresPerLevel) {
        this.maxHighScoresPerLevel = maxHighScoresPerLevel;
    }

    // This method should be used only by SingletonFactory and Unit tests
    void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    @Override
    public void addScoreIfBelongsToHallOfFame(int level, int userId, int score) {
        LevelHighScores levelHighScores = highScoresMap.get(level);
        if (levelHighScores == null) {
            assertHighScoresMapUnderMaxLevels();
            levelHighScores = new LevelHighScores(maxHighScoresPerLevel);
        }
        levelHighScores.addUserScoreIfBelongToHallOfFame(userId, score);
        highScoresMap.put(level, levelHighScores);
    }

    private void assertHighScoresMapUnderMaxLevels() {
        if (highScoresMap.size() >= maxLevels) {
            throw new MaxMemoryReachedException("No more levels allowed in the application");
        }
    }

    @Override
    public List<UserScore> getHighScoreListPerLevel(int level) {
        LevelHighScores levelHighScores = highScoresMap.get(level);
        if (levelHighScores == null) {
            return Collections.emptyList();
        }
        List<UserScore> sortedUserScoresAsc = levelHighScores.getSortedUserScoresAsc();
        Collections.reverse(sortedUserScoresAsc);
        return sortedUserScoresAsc;
    }

}
