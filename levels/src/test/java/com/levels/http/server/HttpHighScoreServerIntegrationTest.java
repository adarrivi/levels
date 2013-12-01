package com.levels.http.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.levels.http.controller.HttpStringResponseController;

public class HttpHighScoreServerIntegrationTest {

    private static final String EMPTY = "";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Map<Integer, String> sessionMap = new HashMap<>();

    // output parameters
    private int responseCode;
    private String responseContent;

    @BeforeClass
    public static void setUpClass() throws IOException {
        HttpHighScoreServer.main(null);
    }

    @AfterClass
    public static void tearDownClass() {
        HttpHighScoreServer.stopServer();
    }

    private void loginUsers(int numberOfUsers) throws IOException {
        for (int i = 0; i < numberOfUsers; i++) {
            requestGet("http://localhost:8081/" + i + "/login");
            verifyResponseType(HttpURLConnection.HTTP_OK);
            sessionMap.put(i, responseContent);

        }
        Assert.assertTrue(numberOfUsers == sessionMap.size());
    }

    private void verifyResponseType(int expectedType) {
        Assert.assertEquals(expectedType, responseCode);
    }

    private void verifyResponseContent(String expectedContent) {
        Assert.assertEquals(expectedContent, responseContent);
    }

    @Test
    public void testHighScoreConnection() throws IOException {
        loginUsers(3);
        String scoreUrl = "http://localhost:8081/1/score?sessionkey=";
        int score = 10;
        for (String sessionKey : sessionMap.values()) {
            requestPost(scoreUrl + sessionKey, Integer.toString(score));
            verifyResponseType(HttpURLConnection.HTTP_OK);
            verifyResponseContent(EMPTY);
            score += 5;
        }
        requestGet("http://localhost:8081/1/highscorelist");
        Assert.assertEquals("2=20,1=15,0=10", responseContent);
    }

    private void requestGet(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpStringResponseController.GET);
        readResponse(connection);
    }

    private void readResponse(HttpURLConnection connection) throws IOException {
        responseCode = connection.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        responseContent = sb.toString();
    }

    private void requestPost(String urlPath, String postContent) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpStringResponseController.POST);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postContent);
        wr.flush();
        wr.close();
        readResponse(connection);
    }

}
