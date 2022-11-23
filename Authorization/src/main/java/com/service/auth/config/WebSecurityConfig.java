package com.service.auth.config;


import com.service.auth.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    private final String[] whitelistUrl = {
            "/user/login",
            "/user/logout",
            "/captcha",
            "/favicon.ico",
            "/rsa/publicKey",
            "/swagger**/**",
            "/webjars/**",
            "/v3/**",
            "/doc.html"
    };
//    @Autowired
//    UserRepositoryOAuth2UserHandler userRepositoryOAuth2UserHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http                //关闭csrf
                .csrf()
                .disable()
                //不通过Session获取SecurityContext
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口,验证码等接口 允许匿名访问
                .antMatchers(whitelistUrl).anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
//        http.authenticationProvider(customAuthenticationProvider);
//        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
//                accessDeniedHandler(accessDeniedHandler);
        http
                .headers()
                .frameOptions()
                .sameOrigin();
        //允许跨域
        http.cors();
//        http.oauth2Login(oauth2login -> {//oauth2登录使用自定义的授权处理器，处理首次登录的用户，持久化等操作。。。
//            SavedUserAuthenticationSuccessHandler successHandler = new SavedUserAuthenticationSuccessHandler();
//            successHandler.setOauth2UserHandler(userRepositoryOAuth2UserHandler);
//            oauth2login.successHandler(successHandler);
//        });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {//装配AuthenticationManager,否则会报找不到bean
        return new ProviderManager(customAuthenticationProvider);
    }

    @Autowired
    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
//        ProviderManager p;
        authenticationManagerBuilder
                .authenticationProvider(customAuthenticationProvider);
    }
}
