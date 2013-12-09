package com.levels.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.ThreadSafe;

import com.levels.dao.LevelScoreDao;
import com.levels.model.LevelHighScores;
import com.levels.model.UserScore;

/**
 * DAO class for the User's High Scores per level, keeping the results stored in
 * memory
 * 
 * @author adarrivi
 * 
 */
@ThreadSafe
final class LevelScoreDaoInMemory implements LevelScoreDao {

    private int maxHighScoresPerLevel;
    private ConcurrentHashMap<Integer, LevelHighScores> highScoresMap = new ConcurrentHashMap<>();

    LevelScoreDaoInMemory() {
        // Only Factory (and Unit tests) should have access to the
        // constructor
    }

    // This method should be used only by Factory and Unit tests
    void setMaxHighScoresPerLevel(int maxHighScoresPerLevel) {
        this.maxHighScoresPerLevel = maxHighScoresPerLevel;
    }

    // This method should be used only by Unit tests
    int getScoresMapSize() {
        return highScoresMap.size();
    }

    @Override
    // The synchronisation of the method guarantees the data integrity of the
    // operation
    public synchronized void addScoreIfBelongsToHallOfFame(int level, int userId, int score) {
        LevelHighScores levelHighScores = highScoresMap.get(level);
        if (levelHighScores == null) {
            levelHighScores = new LevelHighScores(maxHighScoresPerLevel);
        }
        levelHighScores.addUserScoreIfBelongToHallOfFame(userId, score);
        highScoresMap.put(level, levelHighScores);
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
