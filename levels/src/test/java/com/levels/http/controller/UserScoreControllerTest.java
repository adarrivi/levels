package com.levels.http.controller;

import java.util.HashMap;
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

import com.levels.exception.InvalidParameterException;
import com.levels.service.LevelScoreService;
import com.levels.util.PatternVerifierTestBuilder;

public class UserScoreControllerTest {

    private static final String SESSIONKEY = "sessionkey";
    private static final int LEVEL_ID = 1234;
    private static final int SCORE = 30;
    private static final String KEY = "ASDFABCDF";

    @Mock
    private LevelScoreService levelScoreService;

    @InjectMocks
    private HttpStringResponseController victim = new UserScoreController();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int integerFromUrl;
    private Map<String, String> urlParameters;
    private Integer postBody;

    // output parameters
    private String response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processRequest_NullUrlParameters_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenLevel(LEVEL_ID);
        givenNullUrlParams();
        givenScore(SCORE);
        whenProcessRequest();
    }

    private void givenLevel(int level) {
        integerFromUrl = level;
    }

    private void givenNullUrlParams() {
        urlParameters = null;
    }

    private void givenScore(Integer aScore) {
        postBody = aScore;
    }

    private void whenProcessRequest() {
        response = victim.processRequest(urlParameters, postBody, integerFromUrl);
    }

    @Test
    public void processRequest_EmptyUrlParameters_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenLevel(LEVEL_ID);
        givenUrlParameters(new String[0], new String[0]);
        givenScore(SCORE);
        whenProcessRequest();
    }

    private void givenUrlParameters(String[] paramNames, String[] paramValues) {
        urlParameters = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            urlParameters.put(paramNames[i], paramValues[i]);
        }
    }

    @Test
    public void processRequest_NoSessionKeyParameter_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenLevel(LEVEL_ID);
        String[] paramNames = { "param1", "param2" };
        String[] paramValues = { "1", "2" };
        givenUrlParameters(paramNames, paramValues);
        givenScore(SCORE);
        whenProcessRequest();
    }

    @Test
    public void processRequest_NoScoreInBody_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenLevel(LEVEL_ID);
        String[] paramNames = { SESSIONKEY };
        String[] paramValues = { KEY };
        givenUrlParameters(paramNames, paramValues);
        givenScore(null);
        whenProcessRequest();
    }

    @Test
    public void processRequest_AddsScore() {
        givenLevel(LEVEL_ID);
        String[] paramNames = { SESSIONKEY };
        String[] paramValues = { KEY };
        givenUrlParameters(paramNames, paramValues);
        givenScore(SCORE);
        whenProcessRequest();
        thenShouldHaveAddedScore();
    }

    private void thenShouldHaveAddedScore() {
        Mockito.verify(levelScoreService).addScore(LEVEL_ID, SCORE, KEY);
    }

    @Test
    public void processRequest_ResponseShouldBeEmpty() {
        givenLevel(LEVEL_ID);
        String[] paramNames = { SESSIONKEY };
        String[] paramValues = { KEY };
        givenUrlParameters(paramNames, paramValues);
        givenScore(SCORE);
        whenProcessRequest();
        thenResponseShouldBeEmpty();
    }

    private void thenResponseShouldBeEmpty() {
        Assert.assertEquals("", response);
    }

    @Test
    public void verifyValidUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/0/score  ");
        patterVerifierBuilder.addString("/0/score");
        patterVerifierBuilder.addString("/1/score?");
        patterVerifierBuilder.addString("/12345/score?sessionkey=ADBBDCDD");
        patterVerifierBuilder.addString("/12345/score?sessionkey=ADBBDCDD    ");
        patterVerifierBuilder.verifyMatchesAll();
    }

    @Test
    public void verifyInvalidsUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/score");
        patterVerifierBuilder.addString("//score");
        patterVerifierBuilder.addString("/-1/score");
        patterVerifierBuilder.addString("/-12345/score");
        patterVerifierBuilder.addString("/a/score");
        patterVerifierBuilder.addString("/abcde/score");
        patterVerifierBuilder.verifyNoMatches();
    }

    @Test
    public void requestMethod_ShouldReturnPost() {
        Assert.assertEquals(HttpStringResponseController.POST, victim.getRequestMethod());
    }

}
