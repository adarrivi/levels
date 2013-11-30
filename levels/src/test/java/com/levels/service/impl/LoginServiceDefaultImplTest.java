package com.levels.service.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.dao.UserSessionDao;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LoginService;

public class LoginServiceDefaultImplTest {
    private static final String UNIQUE_KEY = "UKDFVADE";
    private static final int USER_ID = 145;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private KeyGenerator keyGenerator;
    @Mock
    private UserSessionDao userSessionDao;

    @InjectMocks
    private LoginService victim = new LoginServiceDefaultImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int userId;

    @Mock
    private UserIdSessionDto modkedSessionDto;

    // output parameters
    private String sessionKey;
    private UserIdSessionDto userSessionDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void givenUserId(int givenUserId) {
        userId = givenUserId;
    }

    private void whenLoginUser() {
        sessionKey = victim.loginUser(userId);
    }

    @Test
    public void loginUser_SavesUserSession() {
        givenUserId(USER_ID);
        whenLoginUser();
        thenNewSessionShouldBeSaved();
    }

    private void thenNewSessionShouldBeSaved() {
        Mockito.verify(userSessionDao).saveOrUpdate(Matchers.eq(USER_ID), Matchers.any(UserSession.class));
    }

    @Test
    public void loginUser_GeneratesNewKey() {
        givenUserId(USER_ID);
        whenLoginUser();
        thenGeneratesNewKey();
    }

    private void thenGeneratesNewKey() {
        Mockito.verify(keyGenerator).generateUniqueKey();
    }

    @Test
    public void loginUser_SetsCurrentDate() {
        givenUserId(USER_ID);
        whenLoginUser();
        thenSetsCurrentDate();
    }

    private void thenSetsCurrentDate() {
        Mockito.verify(dateProvider).getCurrentDate();
    }

    @Test
    public void loginUser_ReturnsUniqueKey() {
        givenUserId(USER_ID);
        givenGeneratedKey(UNIQUE_KEY);
        whenLoginUser();
        thenShouldReturnKey(UNIQUE_KEY);
    }

    private void givenGeneratedKey(String key) {
        Mockito.when(keyGenerator.generateUniqueKey()).thenReturn(key);
    }

    private void thenShouldReturnKey(String expectedKey) {
        Assert.assertEquals(expectedKey, sessionKey);
    }

    @Test
    public void getValidUserIdSessionByKey_NoSessionFound_ReturnsNull() {
        givenNoSessionFound();
        whenGetValidUserIdSessionByKey();
        thenSessionShouldBe(null);
    }

    private void givenNoSessionFound() {
        Mockito.when(userSessionDao.findUserSessionDtoBySessionKey(UNIQUE_KEY)).thenReturn(null);
    }

    private void whenGetValidUserIdSessionByKey() {
        userSessionDto = victim.getValidUserIdSessionByKey(UNIQUE_KEY);
    }

    private void thenSessionShouldBe(UserIdSessionDto expectedSessionDto) {
        Assert.assertEquals(expectedSessionDto, userSessionDto);
    }

    @Test
    public void getValidUserIdSessionByKey_SessionExpired_ReturnsNull() {
        givenExpiredSession(true);
        whenGetValidUserIdSessionByKey();
        thenSessionShouldBe(null);
    }

    private void givenExpiredSession(boolean expired) {
        Mockito.when(modkedSessionDto.hasExpired(Matchers.any(Date.class))).thenReturn(expired);
        Mockito.when(userSessionDao.findUserSessionDtoBySessionKey(UNIQUE_KEY)).thenReturn(modkedSessionDto);
    }

    @Test
    public void getValidUserIdSessionByKey_ReturnsSessionDto() {
        givenExpiredSession(false);
        whenGetValidUserIdSessionByKey();
        thenSessionShouldBe(modkedSessionDto);
    }
}
