package com.levels.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.levels.model.UserSession;
import com.levels.service.DateProvider;
import com.levels.util.ConcurrentExecutor;

public class UserSessionDaoImplIntegrationTest {

    private static final int CONCURRENT_THREADS = 15;
    private static final int EXECUTIONS = 1000;
    private static final int INITIAL_USERS = 1000;

    private UserSessionDaoInMemory victim;
    private ExecutorService executorSerivce;

    @Before
    public void setUp() {
        victim = new UserSessionDaoInMemory();
        victim.setDateProvider(new DateProvider());
        executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    }

    @After
    public void tearDown() {
        executorSerivce.shutdown();
    }

    @Test
    public void verifyConcurrentExpiredDates() {
        loadInitialUsers();
        SaveAndFindAction saveAndFindAction = new SaveAndFindAction(victim, true);
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, saveAndFindAction);
        concurrentExecutor.verifyConcurrentExecution();
        // The last insertion has not been removed
        thenSessionMapSizeShouldBe(1 + INITIAL_USERS);
    }

    private void thenSessionMapSizeShouldBe(int expectedSize) {
        Assert.assertEquals(expectedSize, victim.getSessionMapSize());
    }

    private void loadInitialUsers() {
        Date date = new Date();
        for (int i = 0; i < INITIAL_USERS; i++) {
            String sessionKey = Integer.toString(i);
            victim.saveOrUpdate(i, new UserSession(sessionKey, date));
        }
    }

    private static class SaveAndFindAction implements Runnable {
        private UserSessionDaoInMemory actionVictim;
        private boolean expiredDates;

        public SaveAndFindAction(UserSessionDaoInMemory actionVictim, boolean expiredDates) {
            this.actionVictim = actionVictim;
            this.expiredDates = expiredDates;
        }

        @Override
        public void run() {
            Date expirationDate = new Date();
            if (expiredDates) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -11);
                expirationDate = calendar.getTime();
            }

            for (int i = INITIAL_USERS; i < INITIAL_USERS + EXECUTIONS; i++) {
                String sessionKey = Integer.toString(i);
                actionVictim.saveOrUpdate(i, new UserSession(sessionKey, expirationDate));
                actionVictim.findUserSessionDtoBySessionKey(sessionKey);
            }
        }

    }

    @Test
    public void verifyConcurrentNoExpiredDates() {
        loadInitialUsers();
        ExecutorService executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        SaveAndFindAction saveAndFindAction = new SaveAndFindAction(victim, false);
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, saveAndFindAction);
        concurrentExecutor.verifyConcurrentExecution();
        thenSessionMapSizeShouldBe(INITIAL_USERS + EXECUTIONS);

    }

}
