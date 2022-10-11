package com.service.mis.handler;

import com.alibaba.fastjson.JSON;
import com.service.mis.utils.ResponseResult;

import com.service.mis.utils.WebUtils;
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
    private String msg = "您的权限不足";
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(),msg);
        String json = JSON.toJSONString(result);
        //处理异常
        WebUtils.renderString(response,json,403);
    }
}
