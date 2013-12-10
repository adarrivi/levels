package com.levels.http.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.levels.http.controller.HttpStringResponseController;
import com.levels.util.ConcurrentExecutor;

@Ignore
public class MultipleRequestIntegrationTest {

    private static final int CONCURRENT_THREADS = 20;
    private static final int USERS = 20;
    private static final int LEVELS = 3;
    private ExecutorService executorSerivce;

    @BeforeClass
    public static void setUpClass() throws IOException {
        HttpHighScoreServer.main(null);
    }

    @AfterClass
    public static void tearDownClass() {
        HttpHighScoreServer.stopServer();
    }

    @Before
    public void setUp() {
        executorSerivce = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    }

    @After
    public void tearDown() {
        executorSerivce.shutdown();
    }

    @Test
    public void verifyConcurrency() throws IOException {
        LoginAndScoreAction action = new LoginAndScoreAction();
        ConcurrentExecutor concurrentExecutor = new ConcurrentExecutor(executorSerivce, CONCURRENT_THREADS, action);
        concurrentExecutor.verifyConcurrentExecution();
        for (int level = 1; level <= LEVELS; level++) {
            Assert.assertEquals("20=200,19=190,18=180,17=170,16=160,15=150,14=140,13=130,12=120,11=110,10=100,9=90,8=80,7=70,6=60",
                    requestGet("http://localhost:8081/" + level + "/highscorelist"));
        }
    }

    private class LoginAndScoreAction implements Runnable {

        @Override
        public void run() {
            for (int level = 1; level <= LEVELS; level++) {
                for (int userId = 1; userId <= USERS; userId++) {
                    try {
                        String sessionKey = requestGet("http://localhost:8081/" + userId + "/login");
                        String scoreUrl = "http://localhost:8081/" + level + "/score?sessionkey=";
                        String score = Integer.toString(userId * 10);
                        requestPost(scoreUrl + sessionKey, score);
                    } catch (IOException ex) {
                        // Nothing to do
                    }
                }
            }
        }

    }

    private String requestGet(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpStringResponseController.GET);
        return readResponse(connection);
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        return sb.toString();
    }

    private String requestPost(String urlPath, String postContent) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HttpStringResponseController.POST);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postContent);
        wr.flush();
        wr.close();
        return readResponse(connection);
    }

}
