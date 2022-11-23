package com.service.auth.handler;

//import com.alibaba.fastjson.JSON;
//import com.service.mis.utils.ResponseResult;
//import com.service.mis.utils.WebUtils;

import com.service.auth.utils.ResponseResult;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Setter
    private String message = "您的权限不足";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("gg");

        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(), message);
        System.out.println(result);
//        String json = JSON.toJSONString(result);
//        //处理异常
//        WebUtils.renderString(response,json,403);
    }
}
