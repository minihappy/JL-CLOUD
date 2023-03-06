package com.authorization.service.service;


import com.authorization.service.entity.User;
import com.authorization.service.utils.Result;

public interface LoginService {
    Result login(User user);

    Result logout();
}
