package com.service.mis.filter;

import com.service.mis.expresion.CaptchaException;
import com.service.mis.handler.AuthenticationEntryPointImpl;
import com.service.mis.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisCache redisCache;
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    private boolean isBlank(String s) {
        if (StringUtils.isEmpty(s) || StringUtils.hasText(" ")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();

        if ("/user/login".equals(url) && request.getMethod().equals("POST")) {
            try {
                validate(request);
            } catch (CaptchaException c) {
                authenticationEntryPoint.setMsg("验证码错误");
                throw c;
//                throw new RuntimeException("验证码错误异常");
            }
        }
        filterChain.doFilter(request, response);
    }

    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {

        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");
        if (isBlank(code) || isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }
        if (!code.equals(redisCache.getCacheMapValue("captcha", key))) {
            throw new CaptchaException("验证码错误");
        }
        redisCache.delCacheMapValue("captcha", key);
    }
}
