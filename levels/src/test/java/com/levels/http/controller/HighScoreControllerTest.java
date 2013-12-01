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

import com.levels.service.LevelScoreService;
import com.levels.util.PatternVerifierTestBuilder;

public class HighScoreControllerTest {

    private static final int LEVEL_ID = 1234;
    private static final String HIGH_SCORE = "4117=134,221=20";

    @Mock
    private LevelScoreService levelScoreService;

    @InjectMocks
    private HttpStringResponseController victim = new HighScoreController();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private int integerFromUrl;

    // output parameters
    private String response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void processRequest_ReturnsLevelScoreServiceResult() {
        givenLevel(LEVEL_ID);
        givenHighScoreResult(HIGH_SCORE);
        whenProcessRequest();
        thenKeyShouldBe(HIGH_SCORE);
    }

    private void givenLevel(int userId) {
        integerFromUrl = userId;
    }

    private void givenHighScoreResult(String scoreResult) {
        Mockito.when(levelScoreService.getHighScoreListPerLevel(LEVEL_ID)).thenReturn(HIGH_SCORE);
    }

    private void whenProcessRequest() {
        response = victim.processRequest(null, null, integerFromUrl);
    }

    private void thenKeyShouldBe(String expectedKey) {
        Assert.assertEquals(expectedKey, response);
    }

    @Test
    public void verifyValidUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/0/highscorelist");
        patterVerifierBuilder.addString("/1/highscorelist");
        patterVerifierBuilder.addString("/12345/highscorelist");
        patterVerifierBuilder.addString("/12345/highscorelist      ");
        patterVerifierBuilder.verifyMatchesAll();
    }

    @Test
    public void verifyInvalidsUrls() {
        PatternVerifierTestBuilder patterVerifierBuilder = PatternVerifierTestBuilder.newBuilder(victim.getUrlRegexPattern());
        patterVerifierBuilder.addString("/highscorelist");
        patterVerifierBuilder.addString("//highscorelist");
        patterVerifierBuilder.addString("/-1/highscorelist");
        patterVerifierBuilder.addString("/-12345/highscorelist");
        patterVerifierBuilder.addString("/a/highscorelist");
        patterVerifierBuilder.addString("/abcde/highscorelist");
        patterVerifierBuilder.verifyNoMatches();
    }

    @Test
    public void requestMethod_ShouldReturnGet() {
        Assert.assertEquals(HttpStringResponseController.GET, victim.getRequestMethod());
    }

}
