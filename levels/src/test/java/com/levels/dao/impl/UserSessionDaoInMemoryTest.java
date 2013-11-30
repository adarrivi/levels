package com.levels.dao.impl;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.exception.MaxMemoryReachedException;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;

public class UserSessionDaoInMemoryTest {

    private static final int USER_ID = 100;

    private static final int MAX_SESSIONS_ALLOWED = 5;

    private static final String SESSION_KEY = "UTDSFGK";

    @Mock
    private Map<Integer, UserSession> sessionMap;

    @InjectMocks
    private UserSessionDaoInMemory victim = new UserSessionDaoInMemory();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private UserSession userSession;
    // output parameters
    private UserIdSessionDto foundUserDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        victim.setMaxUserSessionAllowed(MAX_SESSIONS_ALLOWED);
    }

    @Test
    public void saveOrUpdate_OverMaxSessionLimit_ThrowsEx() {
        expectedException.expect(MaxMemoryReachedException.class);
        givenSessionsPreLoaded(MAX_SESSIONS_ALLOWED);
        givenNewSessionForUser();
        whenSaveOrUpdate();
    }

    private void givenSessionsPreLoaded(int sessions) {
        Mockito.when(sessionMap.size()).thenReturn(sessions);
    }

    private void givenNewSessionForUser() {
        userSession = new UserSession(SESSION_KEY + USER_ID, new Date());
    }

    private void whenSaveOrUpdate() {
        victim.saveOrUpdate(USER_ID, userSession);
    }

    @Test
    public void saveOrUpdate_UnderMaxSessionLimit() {
        givenSessionsPreLoaded(MAX_SESSIONS_ALLOWED - 1);
        givenNewSessionForUser();
        whenSaveOrUpdate();
    }

    @Test
    public void saveOrUpdate_PutSessionInMap() {
        givenNewSessionForUser();
        whenSaveOrUpdate();
        thenSessionShouldBePutInMap();
    }

    private void thenSessionShouldBePutInMap() {
        Mockito.verify(sessionMap).put(USER_ID, userSession);
    }

    @Test
    public void findUserSessionDtoBySessionKey_KeyNotFound_ReturnsNull() {
        givenSessionKeyNotFound();
        whenFindUserSessionDtoBySessionKey();
        thenFoundUserIdShouldBeNull();
    }

    private void thenFoundUserIdShouldBeNull() {
        Assert.assertNull(foundUserDto);
    }

    private void givenSessionKeyNotFound() {
        Mockito.when(sessionMap.entrySet()).thenReturn(Collections.<Entry<Integer, UserSession>> emptySet());
    }

    private void whenFindUserSessionDtoBySessionKey() {
        foundUserDto = victim.findUserSessionDtoBySessionKey(SESSION_KEY);
    }

    private void thenFoundUserIdShouldBe(Integer expectedValue) {
        Assert.assertTrue(expectedValue == foundUserDto.getUserId());
    }

    @Test
    public void findUserSessionDtoBySessionKey_KeyFound_ReturnsUserId() {
        givenSessionKeyFound();
        whenFindUserSessionDtoBySessionKey();
        thenFoundUserIdShouldBe(USER_ID);
    }

    private void givenSessionKeyFound() {
        UserSession session = new UserSession(SESSION_KEY, new Date());
        Entry<Integer, UserSession> entry = new AbstractMap.SimpleEntry<Integer, UserSession>(USER_ID, session);
        Mockito.when(sessionMap.entrySet()).thenReturn(Collections.singleton(entry));
    }
}
