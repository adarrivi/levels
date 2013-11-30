package com.levels.model;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.levels.util.EqualsHashTestVerifier;

public class UserSessionTest {

    private static final String SESSION_KEY = "UICSNDK";
    private static final String DIFFERENT_KEY = "ACDBGRE";
    private static final Date NOW = new Date();

    private UserSession victim;

    // input parameters
    private Date currentDate;
    private UserSession equalsUserSession;
    private UserSession differentUserSession;
    // Output parameters
    private String key;
    private boolean hasExpired;

    @Test
    public void hasExpired_CreationDate_ReturnsFalse() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(0, 0));
        whenHasExpired();
        thenSessionShouldBeExpired(false);
    }

    private void givenCurrentDate(Date date) {
        currentDate = date;
    }

    private void givenUserSession(String key, Date creationDate) {
        victim = new UserSession(key, creationDate);
    }

    private void whenHasExpired() {
        hasExpired = victim.hasExpired(currentDate);
    }

    private void thenSessionShouldBeExpired(boolean expectedValue) {
        Assert.assertEquals(expectedValue, hasExpired);
    }

    @Test
    public void hasExpired_05m00sAfter_ReturnsFalse() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(5, 0));
        whenHasExpired();
        thenSessionShouldBeExpired(false);
    }

    private Date getDateAfterNow(int minutes, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(NOW);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    @Test
    public void hasExpired_00m01s_ReturnsFalse() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(0, 1));
        whenHasExpired();
        thenSessionShouldBeExpired(false);
    }

    @Test
    public void hasExpired_09m59sAfter_ReturnsFalse() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(9, 59));
        whenHasExpired();
        thenSessionShouldBeExpired(false);
    }

    @Test
    public void hasExpired_10m00sAfter_ReturnsFalse() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(10, 00));
        whenHasExpired();
        thenSessionShouldBeExpired(false);
    }

    @Test
    public void hasExpired_10m01sAfter_ReturnsTrue() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(10, 1));
        whenHasExpired();
        thenSessionShouldBeExpired(true);
    }

    @Test
    public void hasExpired_15m00sAfter_ReturnsTrue() {
        givenUserSession(SESSION_KEY, NOW);
        givenCurrentDate(getDateAfterNow(15, 0));
        whenHasExpired();
        thenSessionShouldBeExpired(true);
    }

    @Test
    public void getKey_ReturnsSetKey() {
        givenUserSession(SESSION_KEY, NOW);
        whenGetKey();
        thenKeyShouldBe(SESSION_KEY);
    }

    private void whenGetKey() {
        key = victim.getKey();
    }

    private void thenKeyShouldBe(String expectedKey) {
        Assert.assertEquals(expectedKey, key);
    }

    @Test
    public void differentCreationDate_DoesNotAffectEquals() {
        givenDifferentCreationDateEntities();
        EqualsHashTestVerifier verifier = new EqualsHashTestVerifier(victim, equalsUserSession, differentUserSession);
        verifier.verify();
    }

    private void givenDifferentCreationDateEntities() {
        givenUserSession(SESSION_KEY, NOW);
        Date differentDate = new Date();
        equalsUserSession = new UserSession(SESSION_KEY, differentDate);
        differentUserSession = new UserSession(DIFFERENT_KEY, differentDate);
    }

    @Test
    public void differentKey_AffectsEquals() {
        givenDifferentKeySameCreationDateEntities();
        EqualsHashTestVerifier verifier = new EqualsHashTestVerifier(victim, equalsUserSession, differentUserSession);
        verifier.verify();
    }

    private void givenDifferentKeySameCreationDateEntities() {
        givenUserSession(SESSION_KEY, NOW);
        Date differentDate = new Date();
        equalsUserSession = new UserSession(SESSION_KEY, differentDate);
        differentUserSession = new UserSession(DIFFERENT_KEY, NOW);
    }

}
