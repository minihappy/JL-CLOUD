package com.service.gateway.Authorization;

import cn.hutool.core.convert.Convert;
import com.service.gateway.constant.AuthConstant;
import com.service.gateway.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hjl
 * @date 2022/9/20 11:48
 * @description 自定义鉴权操作实现
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
        if (obj == null) {
            ScanOptions build = ScanOptions.scanOptions().match(uri.getPath().substring(0, uri.getPath().lastIndexOf("/")) + "*").build();
            Cursor<Map.Entry<Object, Object>> scan = redisTemplate.opsForHash().scan(RedisConstant.RESOURCE_ROLES_MAP, build);
            while (scan.hasNext()) {
                Map.Entry<Object, Object> next = scan.next();
                obj = next.getValue();
            }
        }
        List<String> authorities = Convert.toList(String.class, obj);
        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                //判断认证是否成功
                .filter(Authentication::isAuthenticated)
                //获取认证后的全部权限
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                //如果包含有效的权限就返回true
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
