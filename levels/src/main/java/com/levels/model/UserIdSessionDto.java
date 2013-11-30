package com.levels.model;

import java.util.Date;

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

}
