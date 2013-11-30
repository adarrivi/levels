package com.levels.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.levels.dao.LevelScoreDao;
import com.levels.exception.InvalidParameterException;
import com.levels.model.UserIdSessionDto;
import com.levels.model.UserScore;
import com.levels.model.UserSession;
import com.levels.service.LevelScoreService;
import com.levels.service.LoginService;

public class LevelScoreServiceDefaultImplTest {

    private static final int LEVEL = 1;
    private static final int USER_ID = 1;
    private static final int SCORE = 30;
    private static final String KEY = "ASRDKDS";

    @Mock
    private LoginService loginService;
    @Mock
    private LevelScoreDao levelScoreDao;
    @InjectMocks
    private LevelScoreService victim = new LevelScoreServiceDefaultImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // input parameters
    private List<UserScore> highScores;
    private UserIdSessionDto userSessionDto;

    // output parameters
    private String csvList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getHighScoreListPerLevel_OScores_ReturnsEmptyString() {
        givenScores(0);
        whenGetHighScoreListPerLevel();
        thenCsvListShouldBe("");
    }

    private void whenGetHighScoreListPerLevel() {
        csvList = victim.getHighScoreListPerLevel(LEVEL);
    }

    private void thenCsvListShouldBe(String expectedList) {
        Assert.assertEquals(expectedList, csvList);
    }

    @Test
    public void getHighScoreListPerLevel_1Score_ReturnsCsvFormat() {
        givenScores(1);
        whenGetHighScoreListPerLevel();
        thenCsvListShouldBe("1=30");
    }

    private void givenScores(int numberOfScores) {
        UserScore score = new UserScore(USER_ID, SCORE);
        highScores = Collections.nCopies(numberOfScores, score);
        Mockito.when(levelScoreDao.getHighScoreListPerLevel(LEVEL)).thenReturn(highScores);
    }

    @Test
    public void getHighScoreListPerLevel_3Scores_ReturnsCsvFormat() {
        givenScores(3);
        whenGetHighScoreListPerLevel();
        thenCsvListShouldBe("1=30,1=30,1=30");
    }

    @Test
    public void addScore_InvalidSessionKey_ThrowsEx() {
        expectedException.expect(InvalidParameterException.class);
        givenInvalidSessionKey();
        whenAddScore();
    }

    private void givenInvalidSessionKey() {
        Mockito.doThrow(InvalidParameterException.class).when(loginService).getValidUserIdSessionByKey(KEY);
    }

    private void whenAddScore() {
        victim.addScore(LEVEL, SCORE, KEY);
    }

    @Test
    public void addScore_AddsUserIdToScore() {
        givenSessionKey();
        whenAddScore();
        thenUserIdShouldBeAddedToScore();
    }

    private void givenSessionKey() {
        userSessionDto = new UserIdSessionDto(USER_ID, new UserSession(KEY, new Date()));
        Mockito.when(loginService.getValidUserIdSessionByKey(KEY)).thenReturn(userSessionDto);
    }

    private void thenUserIdShouldBeAddedToScore() {
        Mockito.verify(levelScoreDao).addScoreIfBelongsToHallOfFame(LEVEL, userSessionDto.getUserId(), SCORE);
    }

}
