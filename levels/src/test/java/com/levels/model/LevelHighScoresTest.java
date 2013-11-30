package com.levels.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LevelHighScoresTest {

    private LevelHighScores victim;

    // output parameters
    private List<UserScore> scores;

    @Test
    public void getSortedUserScoresAsc_Empty_ReturnsEmptyList() {
        givenEmptyScores();
        whenGetSortedUserScoresAsc();
        thenScoresShouldContain(0);
    }

    private void givenEmptyScores() {
        victim = new LevelHighScores(3);
    }

    private void whenGetSortedUserScoresAsc() {
        scores = victim.getSortedUserScoresAsc();
    }

    private void thenScoresShouldContain(int expectedSize) {
        Assert.assertTrue(expectedSize == scores.size());
    }

    @Test
    public void getSortedUserScoresAsc_3Scores_ReturnContains3() {
        givenScores(3);
        whenGetSortedUserScoresAsc();
        thenScoresShouldContain(3);
    }

    private void givenScores(int scoresNumber) {
        givenEmptyScores();
        for (int i = 1; i <= scoresNumber; i++) {
            whenAddScore(i, i * 10);
        }
    }

    private void whenAddScore(int userId, int score) {
        victim.addUserScoreIfBelongToHallOfFame(userId, score);
    }

    @Test
    public void getSortedUserScoresAsc_10Scores_ReturnContains3() {
        givenScores(10);
        whenGetSortedUserScoresAsc();
        thenScoresShouldContain(3);
    }

    @Test
    public void getSortedUserScoresAsc_10Scores_ReturnContains3Highest() {
        givenScores(10);
        whenGetSortedUserScoresAsc();
        thenScoresShouldBeAsc(80, 90, 100);
    }

    private void thenScoresShouldBeAsc(int... scoreList) {
        List<UserScore> sortedUserScoresAsc = victim.getSortedUserScoresAsc();
        Assert.assertEquals(sortedUserScoresAsc.size(), scoreList.length);
        for (int i = 0; i < scoreList.length; i++) {
            Assert.assertTrue(sortedUserScoresAsc.get(i).getScore() == scoreList[i]);
        }
    }

    @Test
    public void getSortedUserScoresAsc_3Scores_ReturnContains3Values() {
        givenScores(3);
        whenGetSortedUserScoresAsc();
        thenScoresShouldBeAsc(10, 20, 30);
    }

    @Test
    public void addUserScoreIfBelongToHallOfFame_DoesNotBelong_IsNotAdded() {
        givenScores(10);
        whenAddScore(11, 10);
        whenGetSortedUserScoresAsc();
        thenScoresShouldBeAsc(80, 90, 100);
    }

    @Test
    public void addUserScoreIfBelongToHallOfFame_SameUserLowerScore_IsNotAdded() {
        givenScores(0);
        whenAddScore(1, 10);
        whenAddScore(1, 5);
        whenGetSortedUserScoresAsc();
        thenScoresShouldBeAsc(10);
    }

    @Test
    public void addUserScoreIfBelongToHallOfFame_SameUserHigherScore_IsAdded() {
        givenScores(0);
        whenAddScore(1, 5);
        whenAddScore(1, 10);
        whenGetSortedUserScoresAsc();
        thenScoresShouldBeAsc(10);
    }

}
