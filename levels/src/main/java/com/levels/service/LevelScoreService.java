package com.levels.service;

public interface LevelScoreService {

    void addScore(int level, int score, String sessionKey);

    String getHighScoreListPerLevel(int level);

}
