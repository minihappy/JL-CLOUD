package com.authorization.service.config;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.authorization.service.entity.User;
import com.authorization.service.jose.Jwks;
//import com.authorization.service.security.FederatedIdentityConfigurer;
//import com.authorization.service.security.FederatedIdentityIdTokenCustomizer;
import com.authorization.service.repository.UserRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;


/**
 * @author hjl
 * @version 1.0
 * @description 自定义授权
 * @date 2022/12/5 23:01
 */
@Configuration
public class AuthorizationServerConfig {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private UserRepository userRepository;

    @Autowired
    private void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/"))
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
//    @Bean
//    @Order(1)
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);//从默认的配置器里面启用默认的安全配置
////        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
////                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
//        http.csrf().disable();
//        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);//配置oauth2的员服务器，并且将jwt配置绑定
////        http.apply(new FederatedIdentityConfigurer());//security应用oauth2自定义的适配器
//        return http.build();
//    }

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
//        return new FederatedIdentityIdTokenCustomizer();
//    }

    // @formatter:off
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // ---------- 1、检查当前客户端是否已注册
        // 操作数据库对象
        RegisteredClient registeredClient = RegisteredClient.withId("hjl.server.cloud")
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("client-app")
//                .clientSecret(passwordEncoder.encode("123456"))
                .clientId("messaging-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://gateway.server.cloud:9201/api/code")
//                .redirectUri("http://auth.server.cloud:9000/oauth2/authorize")
//                .redirectUri("http://gateway.server.cloud:9201/api/oauth2/authorize")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
                .scope("message.read")
                .scope("message.write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())//不用用户手动确认授权
                .build();
        //        RegisteredClient registeredClient_1 = registeredClientRepository.findByClientId(registeredClient.getClientId());
//        if (!(registeredClient_1 instanceof RegisteredClient) || registeredClient_1.getId().isEmpty()) {
//            registeredClientRepository.save(registeredClient);
//        }
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        registeredClientRepository.save(registeredClient);

        return registeredClientRepository;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

                // JWT 构建器
                JwtClaimsSet.Builder claims = context.getClaims();

                // 用户认证
                Authentication principal = context.getPrincipal();

                // 放入用户名
                String name = principal.getName();
                claims.claim("username", name);

                // 放入用户ID
                User users = userRepository.findByUsername(name);
                if (users != null) {
                    // The class with java.lang.Long and name of java.lang.Long is not in
                    // the allowlist.
                    // If you believe this class is safe to deserialize, please provide an
                    // explicit mapping using Jackson annotations or by providing a Mixin.
                    // If the serialization is only done by a trusted source, you can also
                    // enable default typing.
                    // See https://github.com/spring-projects/spring-security/issues/4370
                    // for details
                    claims.claim("user_id", users.getId() + "");
                }

                // 用户权限
                Set<String> authorities = principal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());

                // 客户权限
                Set<String> authorizedScopes = context.getAuthorizedScopes();

                // 合并权限
                authorities.addAll(authorizedScopes);

                // 将合并权限放入 JWT 中
                claims.claim("authorities", authorities);
                /// 微信用户的权限特殊处理
                // 增加微信特有数据
//                if (principal instanceof WeChatMiniProgramAuthenticationToken) {
//                    WeChatMiniProgramAuthenticationToken authenticationToken = (WeChatMiniProgramAuthenticationToken) principal;
//                    // 微信小程序的appid，不能为空
//                    String appid = authenticationToken.getAppid();
//                    // 用户唯一标识，不能为空
//                    String openid = authenticationToken.getOpenid();
//                    // 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a
//                    // href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
//                    // 机制说明</a>。
//                    String unionid = authenticationToken.getUnionid();
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.APPID, appid);
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.OPENID, openid);
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.UNIONID, unionid);
//                }
//                else if (principal instanceof WeChatOplatformWebsiteAuthenticationToken) {
//                    WeChatOplatformWebsiteAuthenticationToken authenticationToken = (WeChatOplatformWebsiteAuthenticationToken) principal;
//                    // 微信公众平台 网站应用 的appid，不能为空
//                    String appid = authenticationToken.getAppid();
//                    // 用户唯一标识，不能为空
//                    String openid = authenticationToken.getOpenid();
//                    // 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a
//                    // href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
//                    // 机制说明</a>。
//                    String unionid = authenticationToken.getUnionid();
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.APPID, appid);
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.OPENID, openid);
//                    claims.claim(OAuth2WeChatMiniProgramParameterNames.UNIONID, unionid);
//                }
            }
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://auth.server.cloud:9000").build();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    // @formatter:on
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
