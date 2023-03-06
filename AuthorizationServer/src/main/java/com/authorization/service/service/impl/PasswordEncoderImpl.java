package com.authorization.service.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 密码编辑起
 *
 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
 */
@Slf4j
@Component
public class PasswordEncoderImpl implements PasswordEncoder {

    private HttpServletRequest request;


    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }


    @Override
    public String encode(CharSequence rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (StringUtils.hasText(rawPassword)) {
            return passwordEncoder.encode(rawPassword.toString());
        } else {
            throw new BadCredentialsException("登录参数不存在密码");
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        boolean matches;

        Map<String, String[]> parameterMap = request.getParameterMap();
        int size = parameterMap.size();
        Set<String> keySet = parameterMap.keySet();
        List<String> list = Arrays.asList(OAuth2ParameterNames.CODE, OAuth2ParameterNames.CLIENT_ID,
                OAuth2ParameterNames.CLIENT_SECRET, OAuth2ParameterNames.REDIRECT_URI, OAuth2ParameterNames.GRANT_TYPE);
        if (size == list.size() && keySet.containsAll(list)) {
            log.info("客户端密码认证");
            matches = passwordEncoder.matches(rawPassword, encodedPassword);
        } else {
            try {
                log.info("比较密码时未启用RSA对密码进行解密");
                matches = passwordEncoder.matches(rawPassword, encodedPassword);
            } catch (Exception e) {
                // 可能根据用户名没有找到用户信息（即：密码），导致比较失败
                throw new BadCredentialsException("比较密码时异常");
            }
            if (!matches) {
                throw new BadCredentialsException("密码错误");
            }
        }

        return matches;
    }

}
