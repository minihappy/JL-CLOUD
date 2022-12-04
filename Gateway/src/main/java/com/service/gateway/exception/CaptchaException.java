package com.service.gateway.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author hjl
 * @version 1.0
 * @description 验证码自定义的异常
 * @date 2022/12/4 16:56
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }
}
