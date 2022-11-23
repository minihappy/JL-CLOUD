package com.service.gateway.Authorization;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.function.Consumer;

/**
 * @author hjl
 * @version 1.0
 * @description 使用EnableRective自定义授权操作，可用于操作前，操作后拦截请求等（security5.8）
 * @date 2022/10/9 17:24
 */

//@Configuration
//@EnableWebFluxSecurity
public class OAuth2LoginSecurityConfig {

//    @Autowired
//    private ReactiveClientRegistrationRepository clientRegistrationRepository;
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange(authorize -> authorize
//                        .anyExchange().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationRequestResolver(authorizationRequestResolver(this.clientRegistrationRepository)
//                        )
//                );
//        return http.build();
//    }
//    private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
//        DefaultServerOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
//        authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());
//        return authorizationRequestResolver;
//    }
//    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {//自定义了客户端带参数授权
//        return customizer -> customizer
//                .additionalParameters(params -> params.put("prompt", "consent"));//设置prompt参数的值为consent
//    }
}
