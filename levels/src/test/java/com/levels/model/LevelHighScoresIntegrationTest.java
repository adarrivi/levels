package com.levels.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.levels.util.ConcurrentExecutor;

public class LevelHighScoresIntegrationTest {

    private static final int CONCURRENT_THREADS = 900;
    private static final int MAX_HSCORES_PER_LEVEL = 15;
    private static final int USERS = 100;
    private ExecutorService executorSerivce;
    private LevelHighScores victim;

    @Before
    public void setUp() {
        victim = new LevelHighScores(MAX_HSCORES_PER_LEVEL);
        executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    }

    @After
    public void tearDown() {
        executorSerivce.shutdown();
    }

    @Test
    public void verifyConcurrencyAccess() {
        ScoreAndGetAction saveAndFindAction = new ScoreAndGetAction(victim);
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, saveAndFindAction);
        concurrentExecutor.verifyConcurrentExecution();
        List<UserScore> sortedUserScoresAsc = victim.getSortedUserScoresAsc();
        verifyScoreResult(sortedUserScoresAsc);
    }

    private void verifyScoreResult(List<UserScore> sortedUserScoresAsc) {
        Assert.assertEquals(MAX_HSCORES_PER_LEVEL, sortedUserScoresAsc.size());
        int index = 0;
        for (int userId = USERS - MAX_HSCORES_PER_LEVEL + 1; userId <= USERS; userId++) {
            Assert.assertEquals(userId, sortedUserScoresAsc.get(index).getUserId());
            Assert.assertEquals(userId * 10, sortedUserScoresAsc.get(index).getScore());
            index++;
        }
    }

    private static class ScoreAndGetAction implements Runnable {
        private LevelHighScores actionVictim;

        public ScoreAndGetAction(LevelHighScores actionVictim) {
            this.actionVictim = actionVictim;
        }

        @Override
        public void run() {
            for (int userId = 1; userId <= USERS; userId++) {
                actionVictim.addUserScoreIfBelongToHallOfFame(userId, userId * 10);
            }
        }

    }

}
