package com.levels.model;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Defines one user score entity
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class UserScore implements Comparable<UserScore> {

    private int userId;
    private int score;

    public UserScore(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(UserScore otherUserScore) {
        return Integer.compare(score, otherUserScore.score);
    }

}
