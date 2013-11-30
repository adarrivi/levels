package com.levels.dao.impl;

import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.exception.MaxSessionsReachedException;
import com.levels.model.UserSession;

public class UserSessionDaoInMemoryTest {

    private static final int USER_ID = 100;

    private static final int MAX_SESSIONS_ALLOWED = 5;

    private static final String KEY_PREFIX = "key";

    @Mock
    private Map<Integer, UserSession> sessionMap;

    @InjectMocks
    private UserSessionDaoInMemory victim = new UserSessionDaoInMemory();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private UserSession userSession;
    // output parameters
    private UserSession sessionFound;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        victim.setMaxUserSessionAllowed(MAX_SESSIONS_ALLOWED);
    }

    @Test
    public void saveOrUpdate_OverMaxSessionLimit_ThrowsEx() {
        expectedException.expect(MaxSessionsReachedException.class);
        givenSessionsPreLoaded(MAX_SESSIONS_ALLOWED);
        givenNewSessionForUser();
        whenSaveOrUpdate();
    }

    private void givenSessionsPreLoaded(int sessions) {
        Mockito.when(sessionMap.size()).thenReturn(sessions);
    }

    private void givenNewSessionForUser() {
        userSession = new UserSession(KEY_PREFIX + USER_ID, new Date());
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
    public void deleteUserSession_SessionRemovedFromMap() {
        givenNewSessionForUser();
        whenDeleteSession();
        thenUserIdSessionShouldBeRemovedFromMap();
    }

    private void whenDeleteSession() {
        victim.deleteUserSession(USER_ID);
    }

    private void thenUserIdSessionShouldBeRemovedFromMap() {
        Mockito.verify(sessionMap).remove(USER_ID);
    }

    @Test
    public void findUserSession_ReturnsSessionMapGet() {
        givenUserSessionFound();
        whenFindUserSession();
        thenSessionFoundShouldBeFromMap();
    }

    private void givenUserSessionFound() {
        givenNewSessionForUser();
        Mockito.when(sessionMap.get(USER_ID)).thenReturn(userSession);
    }

    private void whenFindUserSession() {
        sessionFound = victim.findUserSession(USER_ID);
    }

    private void thenSessionFoundShouldBeFromMap() {
        Mockito.verify(sessionMap).get(USER_ID);
        Assert.assertEquals(sessionFound, userSession);
    }
}
