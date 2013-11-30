package com.levels.dao;

import com.levels.model.UserSession;

public interface UserSessionDao {

    void saveOrUpdate(int userId, UserSession userSession);

    void deleteUserSession(int userId);

    UserSession findUserSession(int userId);

}
