package com.service.mis.service;

import com.service.mis.entity.User;
import com.service.mis.utils.ResponseResult;
import org.springframework.security.core.Authentication;

public interface LoginService {
    ResponseResult login(User user);
    ResponseResult logout();
}
