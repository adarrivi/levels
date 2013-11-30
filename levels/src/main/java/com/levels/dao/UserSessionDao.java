package com.levels.dao;

import com.levels.model.UserIdSessionDto;
import com.levels.model.UserSession;

public interface UserSessionDao {

    void saveOrUpdate(int userId, UserSession userSession);

    UserIdSessionDto findUserSessionDtoBySessionKey(String sessionKey);

}
