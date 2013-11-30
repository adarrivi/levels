package com.levels.service.impl;

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
import com.levels.exception.InvalidParameterException;
import com.levels.model.UserSession;
import com.levels.service.DateProvider;
import com.levels.service.KeyGenerator;
import com.levels.service.LoginService;

public class LoginServiceInMemoryImplTest {
    private static final String UNIQUE_KEY = "UKDFVADE";
    private static final int USER_ID = 145;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private KeyGenerator keyGenerator;
    @Mock
    private UserSessionDao userSessionDao;

    @InjectMocks
    private LoginService victim = new LoginServiceInMemoryImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int userId;

    // output parameters
    private String sessionKey;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loginUser_NegativeUser_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenUserId(-USER_ID);
        whenLoginUser();
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
}
