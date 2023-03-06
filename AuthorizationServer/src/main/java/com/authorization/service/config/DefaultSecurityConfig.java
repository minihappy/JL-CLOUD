package com.authorization.service.config;

/**
 * @author hjl
 * @version 1.0
 * @description 默认的
 * @date 2022/12/5 23:40
 */

//import com.authorization.service.security.FederatedIdentityConfigurer;
//import com.authorization.service.security.UserRepositoryOAuth2UserHandler;
//import com.authorization.service.service.impl.CustomAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfig {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    @Qualifier("authenticationSuccessHandlerImpl")
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }
//    @Bean
//    public PasswordEncoder bcryptEncoder() {
//        return new BCryptPasswordEncoder();
//    }//由于分布式健全统一使用授权中心，该服务的security取消，新增密码加密器在此处使用

    //    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;
    private final String[] whitelistUrl = {
            "/user/login",
            "/user/logout",
            "/captcha",
            "/favicon.ico",
            "/rsa/publicKey",
            "/swagger**/**",
            "/webjars/**",
            "/assets/**",
            "/v3/**",
            "/doc.html",
            "/login",
            "/code",
            "/oauth2/code",
            "/oauth2/token",
            "/oauth2/check_token",
            "/oauth2/jwks",
            "/oauth2/code",
            "/oauth2/authorize",
    };

    // @formatter:off

    /**
     * 用于身份认证的spring security过滤器链
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {//默认使用的登陆器（非oauth2）
        http.csrf().disable();
        http
                .authorizeHttpRequests(authorize ->
                                authorize
                                        // 对于登录接口,验证码等接口 允许匿名访问
                                        .mvcMatchers(whitelistUrl).permitAll()
                                        // 除上面外的所有请求全部需要鉴权认证
                                        .anyRequest().authenticated()
                        // 除上面外的所有请求全部需要鉴权认证
                )
        ;
        http.formLogin(formLogin -> formLogin
//                // 登录页面地址
//                .loginPage("/")
//                // 登录请求地址
//                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                // 已上地址，允许任何人访问
                .permitAll());


        return http.build();
    }

    // @formatter:on

    // @formatter:off
//    @Bean
//    public AuthenticationManager authenticationManagerBean() {//装配AuthenticationManager,否则会报找不到bean
//        return new ProviderManager(customAuthenticationProvider);
//    }
//
//    @Autowired
//    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
////        ProviderManager p;
//        authenticationManagerBuilder
//                .authenticationProvider(customAuthenticationProvider);
//    }
    // @formatter:on

}
