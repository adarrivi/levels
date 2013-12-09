package com.levels.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.levels.model.UserScore;

public class LevelScoreDaoInMemoryTest {

    private LevelScoreDaoInMemory victim = new LevelScoreDaoInMemory();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int level;
    private int userId;
    private int score;

    // output parameters
    private List<UserScore> highScoreList;

    @Before
    public void setUp() {
        victim.setMaxHighScoresPerLevel(5);
    }

    private void givenScore(int aLevel, int anUserId, int aScore) {
        level = aLevel;
        userId = anUserId;
        score = aScore;
    }

    private void whenAddScoreIfBelongsToHallOfFame() {
        victim.addScoreIfBelongsToHallOfFame(level, userId, score);
    }

    @Test
    public void getHighScoreListPerLevel_2SameLevel_ReturnsScores() {
        givenScore(1, 1, 10);
        whenAddScoreIfBelongsToHallOfFame();
        givenScore(1, 2, 20);
        whenAddScoreIfBelongsToHallOfFame();
        givenLevel(1);
        whenGetHighScoreListPerLevel();
        thenScoresPerLevelShouldBeDesc(20, 10);
    }

    private void whenGetHighScoreListPerLevel() {
        highScoreList = victim.getHighScoreListPerLevel(level);
    }

    private void thenScoresPerLevelShouldBeDesc(int... scores) {
        Assert.assertEquals(scores.length, highScoreList.size());
        for (int i = 0; i < scores.length; i++) {
            Assert.assertEquals(scores[i], highScoreList.get(i).getScore());
        }
    }

    @Test
    public void getHighScoreListPerLevel_2SameLevelWithOtherLevels_ReturnsScores() {
        givenScore(1, 1, 10);
        whenAddScoreIfBelongsToHallOfFame();
        givenScore(1, 2, 20);
        whenAddScoreIfBelongsToHallOfFame();
        givenScore(2, 2, 30);
        whenAddScoreIfBelongsToHallOfFame();
        givenLevel(1);
        whenGetHighScoreListPerLevel();
        thenScoresPerLevelShouldBeDesc(20, 10);
    }

    private void givenLevel(int aLevel) {
        level = aLevel;
    }

    @Test
    public void getHighScoreListPerLevel_EmptyLevel_ReturnsEmpty() {
        whenGetHighScoreListPerLevel();
        thenHighScoresShouldBeEmpty();
    }

    private void thenHighScoresShouldBeEmpty() {
        Assert.assertTrue(highScoreList.isEmpty());
    }
}
