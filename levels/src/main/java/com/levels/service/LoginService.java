package com.levels.service;

import com.levels.model.UserIdSessionDto;

public interface LoginService {

    String loginUser(int userId);

    UserIdSessionDto getValidUserIdSessionByKey(String key);
}
