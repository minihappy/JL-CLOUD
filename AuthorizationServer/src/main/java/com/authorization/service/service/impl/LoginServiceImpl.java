package com.authorization.service.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.authorization.service.entity.User;
import com.authorization.service.properties.ClientProperties;
import com.authorization.service.service.LoginService;
import com.authorization.service.utils.JwtUtil;
import com.authorization.service.utils.RedisCache;
import com.authorization.service.utils.Result;
import com.nimbusds.jose.jwk.RSAKey;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.security.KeyPair;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    public void initAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    @Autowired
    public void initClientProperties(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    private RedisCache redisCache;

    private ClientProperties clientProperties;

    @Autowired
    public void initRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @SneakyThrows
    @Override
    public Result login(User user) {
        UUID uuid = UUID.randomUUID();
        return Result.success(200, "登陆成功","http://gateway.server.cloud:9201/api/oauth2/authorize?client_id=messaging-client&response_type=code&scope=message.read%20message.write&redirect_uri=http://gateway.server.cloud:9201/api/code&state="+uuid);
    }

    @Override
    public Result logout() {//退出不带任何参数，从同一个请求里面获取身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//从Security中获取身份
        User loginUser = (User) authentication.getPrincipal();//根据身份获取User实体
        Long id = loginUser.getId();//获取当前的id
        redisCache.deleteObject("login:" + id);//将id从redis中删除，，这样JWT过滤器在过滤请求的时候就会找不到redis中的登录信息，从而判断登录失败
        return Result.success(200, "退出成功", null);
    }
}
