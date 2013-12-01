package com.levels.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the highest users score per level.
 * 
 * @author adarrivi
 * 
 */
public class LevelHighScores {

    private static final Logger LOG = LoggerFactory.getLogger(LevelHighScores.class);

    private int maxHighScoresPerLevel;

    private Map<Integer, UserScore> userHighScoreMap = new ConcurrentHashMap<>();

    public LevelHighScores(int maxHighScoresPerLevel) {
        this.maxHighScoresPerLevel = maxHighScoresPerLevel;
    }

    public void addUserScoreIfBelongToHallOfFame(int userId, int newScore) {
        UserScore previousScoreForUser = userHighScoreMap.get(userId);
        if (previousScoreForUser != null && previousScoreForUser.getScore() < newScore) {
            int previousScore = previousScoreForUser.getScore();
            previousScoreForUser.setScore(newScore);
            userHighScoreMap.put(userId, previousScoreForUser);
            LOG.debug("User {} has beaten his best score {}: {}", userId, previousScore, newScore);
            return;
        }

        UserScore globalLowestScore = getGlobalLowestScore();
        if (globalLowestScore == null || globalLowestScore.getScore() < newScore) {
            userHighScoreMap.put(userId, new UserScore(userId, newScore));
            trimUserHighScoreMap(globalLowestScore);
            LOG.debug("User {} has entered the Hall of Fame with a score: {}", userId, newScore);
            return;
        }
        LOG.debug("User {} has not scored high enough: {}", userId, newScore);
    }

    private void trimUserHighScoreMap(UserScore globalLowestScore) {
        if (userHighScoreMap.size() > maxHighScoresPerLevel && globalLowestScore != null) {
            userHighScoreMap.remove(globalLowestScore.getUserId());
            LOG.debug("User {} has been kicked out from the Hall of Fame with a score: {}", globalLowestScore.getUserId(),
                    globalLowestScore.getScore());
        }
    }

    private UserScore getGlobalLowestScore() {
        List<UserScore> scores = getSortedUserScoresAsc();
        if (scores.isEmpty()) {
            return null;
        }
        return scores.get(0);
    }

    public List<UserScore> getSortedUserScoresAsc() {
        List<UserScore> scores = new ArrayList<>(userHighScoreMap.values());
        Collections.sort(scores);
        return scores;
    }

}
