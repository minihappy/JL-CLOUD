package com.service.auth.service;


import com.service.auth.entity.User;
import com.service.auth.utils.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
