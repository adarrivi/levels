package com.levels.http.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.service.LoginService;
import com.levels.util.PatternVerifierTestBuilder;

public class LoginControllerTest {

    private static final int USER_ID = 1234;
    private static final String KEY = "ASDFABCDF";

    @Mock
    private LoginService loginService;

    @InjectMocks
    private HttpStringResponseController victim = new LoginController();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int integerFromUrl;

    // output parameters
    private String key;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void whenProcessRequest() {
        key = victim.processRequest(null, null, integerFromUrl);
    }

    @Test
    public void processRequest_ReturnsLoginServiceKey() {
        givenUserParameter(USER_ID);
        givenKeyFromService(KEY);
        whenProcessRequest();
        thenKeyShouldBe(KEY);
    }

    private void givenUserParameter(int userId) {
        integerFromUrl = userId;
    }

    private void givenKeyFromService(String aKey) {
        Mockito.when(loginService.loginUser(USER_ID)).thenReturn(aKey);
    }

    private void thenKeyShouldBe(String expectedKey) {
        Assert.assertEquals(expectedKey, key);
    }

    @Test
    public void verifyValidUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/0/login");
        patterVerifierBuilder.addString("/1/login");
        patterVerifierBuilder.addString("/12345/login");
        patterVerifierBuilder.addString("/12345/login      ");
        patterVerifierBuilder.verifyMatchesAll();
    }

    @Test
    public void verifyInvalidsUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/login");
        patterVerifierBuilder.addString("//login");
        patterVerifierBuilder.addString("/-1/login");
        patterVerifierBuilder.addString("/-12345/login");
        patterVerifierBuilder.addString("/a/login");
        patterVerifierBuilder.addString("/abcde/login");
        patterVerifierBuilder.verifyNoMatches();
    }

    @Test
    public void requestMethod_ShouldReturnGet() {
        Assert.assertEquals(HttpStringResponseController.GET, victim.getRequestMethod());
    }

}
