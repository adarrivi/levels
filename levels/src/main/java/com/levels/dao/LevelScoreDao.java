package com.levels.dao;

import java.util.List;

import com.levels.model.UserScore;

public interface LevelScoreDao {

    void addScoreIfBelongsToHallOfFame(int level, int userId, int score);

    List<UserScore> getHighScoreListPerLevel(int level);

}
