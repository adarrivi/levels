package com.levels.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.levels.dao.UserSessionDao;
import com.levels.dao.impl.DaoFactory;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LoginService;
import com.levels.util.ConcurrentExecutor;

public class LevelScoreServiceDefaultImplIntegrationTest {

    private static final int CONCURRENT_THREADS = 1000;
    private static final int USERS = 100;
    private static final int LEVELS = 100;
    private ExecutorService executorSerivce;

    private LevelScoreServiceDefaultImpl victim;
    private LoginService loginService;

    @Before
    public void setUp() {
        victim = new LevelScoreServiceDefaultImpl();
        DateProvider dateProvider = new DateProvider();
        DaoFactory daoFactory = new DaoFactory();
        UserSessionDao userSessionDao = daoFactory.createUserSessionDao(dateProvider);
        victim.setLevelScoreDao(daoFactory.createLevelScoreDao());
        ServiceFactory serviceFactory = new ServiceFactory();
        KeyGenerator keyGenerator = serviceFactory.createKeyGenerator();
        victim.setLoginService(serviceFactory.createLoginService(userSessionDao, dateProvider, keyGenerator));
        loginService = serviceFactory.createLoginService(userSessionDao, dateProvider, keyGenerator);
        executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    }

    @After
    public void tearDown() {
        executorSerivce.shutdown();
    }

    @Test
    public void verifyConcurrentAcess() {
        LoginAndScoreAction saveAndFindAction = new LoginAndScoreAction(victim, loginService);
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, saveAndFindAction);
        concurrentExecutor.verifyConcurrentExecution();
        for (int level = 1; level <= LEVELS; level++) {
            Assert.assertEquals(
                    "100=1000,99=990,98=980,97=970,96=960,95=950,94=940,93=930,92=920,91=910,90=900,89=890,88=880,87=870,86=860",
                    victim.getHighScoreListPerLevel(level));
        }
    }

    private static class LoginAndScoreAction implements Runnable {
        private LevelScoreServiceDefaultImpl actionVictim;
        private LoginService actionLoginService;

        public LoginAndScoreAction(LevelScoreServiceDefaultImpl actionVictim, LoginService actionLoginService) {
            this.actionVictim = actionVictim;
            this.actionLoginService = actionLoginService;
        }

        @Override
        public void run() {
            for (int level = 1; level <= LEVELS; level++) {
                for (int userId = 1; userId <= USERS; userId++) {
                    String sessionKey = actionLoginService.loginUser(userId);
                    actionVictim.addScore(level, userId * 10, sessionKey);
                }
            }
        }
    }

}
