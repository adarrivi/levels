package com.levels.http.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.exception.InvalidParameterException;
import com.levels.service.LoginService;
import com.sun.net.httpserver.HttpExchange;

public class LoginControllerTest {

    private static final int USER_ID = 1234;
    private static final String KEY = "ASDFABCDF";

    @Mock
    private LoginService loginService;
    @Mock
    private ParameterVerifier parameterVerifier;

    @InjectMocks
    private HttpStringController victim = new LoginController();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    @Mock
    private HttpExchange exchange;

    // output parameters
    private String key;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processRequest_InvalidParameter_ThrowsEx() throws URISyntaxException {
        expectedException.expect(InvalidParameterException.class);
        givenInvalidParameter();
        givenUrlRequest("/" + USER_ID + "/login");
        whenProcessRequest();
    }

    private void givenInvalidParameter() {
        Mockito.doThrow(InvalidParameterException.class).when(parameterVerifier).getValueAsUnsignedInt(Integer.toString(USER_ID));
    }

    private void givenUrlRequest(String url) throws URISyntaxException {
        URI uri = new URI(url);
        Mockito.when(exchange.getRequestURI()).thenReturn(uri);
    }

    private void whenProcessRequest() {
        key = victim.processRequest(exchange, null);
    }

    @Test
    public void processRequest_ReturnsLoginServiceKey() throws URISyntaxException {
        givenUrlRequest("/" + USER_ID + "/login");
        givenValidParameter(USER_ID);
        givenKeyFromService(KEY);
        whenProcessRequest();
        thenKeyShouldBe(KEY);
    }

    private void givenValidParameter(int userId) {
        Mockito.when(parameterVerifier.getValueAsUnsignedInt(Integer.toString(userId))).thenReturn(userId);
    }

    private void givenKeyFromService(String aKey) {
        Mockito.when(loginService.loginUser(USER_ID)).thenReturn(aKey);
    }

    private void thenKeyShouldBe(String expectedKey) {
        Assert.assertEquals(expectedKey, key);
    }
}
