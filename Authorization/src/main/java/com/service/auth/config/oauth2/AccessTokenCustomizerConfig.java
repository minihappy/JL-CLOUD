package com.service.auth.config.oauth2;

import com.service.auth.entity.Authority;
import com.service.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

/**
 * @author hjl
 * @version 1
 * @description 自定义配置oauth2的认证token
 * @date 2022/10/20 20:45
 */
@Configuration(proxyBeanMethods = false)
public class AccessTokenCustomizerConfig {

    @Autowired
    RoleRepository roleRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claim -> {
                    claim.put("authorities", roleRepository.findByName(context.getPrincipal().getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_OPERATION"))
                            .getAuthority().stream().map(Authority::getName).collect(Collectors.toSet()));
                });
            }
        };
    }
}
