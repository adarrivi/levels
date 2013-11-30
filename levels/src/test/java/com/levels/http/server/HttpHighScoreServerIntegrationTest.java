package com.levels.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HttpHighScoreServerIntegrationTest {

    private static final int NUMBER_OF_USERS = 3;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Map<Integer, String> sessionMap = new HashMap<>();

    @BeforeClass
    public static void setUpClass() throws IOException {
        HttpHighScoreServer.main(null);
    }

    @AfterClass
    public static void tearDownClass() {
        HttpHighScoreServer.stopServer();
    }

    @Test
    public void testLoginConnection() throws IOException {
        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            loginUser(i);
        }
        Assert.assertTrue(NUMBER_OF_USERS == sessionMap.size());
    }

    private void loginUser(int userId) throws IOException {
        String sesionKey = connect("http://localhost:8081/" + userId + "/login");
        sessionMap.put(userId, sesionKey);
    }

    private String connect(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return in.readLine();
    }

    @Test
    public void testUrlNotFound() throws IOException {
        expectBadRequestResponse();
        connect("http://localhost:8081/urlNotFound/test");
    }

    private void expectBadRequestResponse() {
        expectedException.expect(IOException.class);
        expectedException.expectMessage("Server returned HTTP response code: 400");
    }

}
