package com.levels.dao.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.levels.model.UserScore;
import com.levels.util.ConcurrentExecutor;

public class LevelScoreDaoInMemoryIntegrationTest {

    private static final int MAX_HSCORES_PER_LEVEL = 15;
    private static final int CONCURRENT_THREADS = 200;
    private static final int INITIAL_USERS = 15;
    private static final int LEVELS = 100;

    private LevelScoreDaoInMemory victim;
    private ExecutorService executorSerivce;

    @Before
    public void setUp() {
        victim = new LevelScoreDaoInMemory();
        victim.setMaxHighScoresPerLevel(MAX_HSCORES_PER_LEVEL);
        executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    }

    @After
    public void tearDown() {
        executorSerivce.shutdown();
    }

    @Test
    public void verifyConcurrentAddScores() {
        ScoreAndGetAction saveAndFindAction = new ScoreAndGetAction(victim);
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, saveAndFindAction);
        concurrentExecutor.verifyConcurrentExecution();
        thenLevelsScoreMapSizeShouldBe(LEVELS);
    }

    private void thenLevelsScoreMapSizeShouldBe(int expectedSize) {
        Assert.assertEquals(expectedSize, victim.getScoresMapSize());
        for (int level = 0; level < expectedSize; level++) {
            List<UserScore> highScoreListPerLevel = victim.getHighScoreListPerLevel(level);
            Assert.assertEquals(MAX_HSCORES_PER_LEVEL, highScoreListPerLevel.size());
        }
    }

    private static class ScoreAndGetAction implements Runnable {
        private LevelScoreDaoInMemory actionVictim;

        public ScoreAndGetAction(LevelScoreDaoInMemory actionVictim) {
            this.actionVictim = actionVictim;
        }

        @Override
        public void run() {
            for (int level = 0; level < LEVELS; level++) {
                for (int userId = 0; userId < INITIAL_USERS; userId++) {
                    actionVictim.addScoreIfBelongsToHallOfFame(level, userId, userId * 10);
                    actionVictim.getHighScoreListPerLevel(level);
                }
            }
        }

    }

}
