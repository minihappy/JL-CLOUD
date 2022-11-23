package com.service.auth.handler;

import com.service.auth.utils.ResponseResult;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Setter
    private String message = "登录失败请检查登录信息";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), message);
//        String json = JSON.toJSONString(result);
//        //处理异常
//        WebUtils.renderString(response, json, 401);
        System.out.println(result);
        System.out.println("gg");
    }
}