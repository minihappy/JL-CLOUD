package com.service.auth.config.oauth2;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;


/**
 * @author hjl
 * @version 1.0
 * @description 使用EnableRective自定义授权操作，可用于操作前，操作后拦截请求等（security5.8）
 * @date 2022/10/9 17:24
 */

//@EnableWebFluxSecurity
public class OAuth2LoginSecurityConfig {
    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {//将客户端封装成反应式客户端注册仓库
        return new InMemoryReactiveClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {//谷歌客户端配置
        return ClientRegistration.withRegistrationId("google")
                .clientId("738852566075-p2nhpec2ouscj66led6ql2imtmnfssmu.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()))
        ;
        return http.build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {//退出支持
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(this.clientRegistrationRepository());

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcLogoutSuccessHandler;
    }

    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {//委托授权，根据
        final OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            return delegate.loadUser(userRequest)
                    .flatMap((oidcUser) -> {
                        OAuth2AccessToken accessToken = userRequest.getAccessToken();
                        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

                        // TODO
                        // 1) Fetch the authority information from the protected resource using accessToken
                        // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
                        // 3) Create a copy of oidcUser but use the mappedAuthorities instead

                        // 1)使用accessToken从受保护资源获取权限信息
                        // 2)将权限信息映射到一个或多个GrantedAuthority，并将其添加到mapped authority
                        // 3)创建一个oidcUser的副本，但使用mappedAuthorities
                        oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());

                        return Mono.just(oidcUser);
                    });
        };
    }
}
