package com.levels.model;

import java.util.Date;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Holds together the userId and the session. This class is needed to simplify
 * the Service/DAO logic and to save accesses to DAO
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class UserIdSessionDto {

    private int userId;
    private UserSession session;

    public UserIdSessionDto(int userId, UserSession session) {
        this.userId = userId;
        this.session = session;
    }

    public int getUserId() {
        return userId;
    }

    public boolean hasExpired(Date currentDate) {
        return session.hasExpired(currentDate);
    }

    public String getKey() {
        return session.getKey();
    }

}
