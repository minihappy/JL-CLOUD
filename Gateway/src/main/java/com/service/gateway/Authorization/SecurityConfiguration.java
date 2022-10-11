package com.service.gateway.Authorization;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author hjl
 * @version 1.0
 * @description 使用EnableRective自定义授权操作，可用于操作前，操作后拦截请求等（security5.8）
 * @date 2022/10/9 17:24
 */

//@Configuration
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class SecurityConfiguration {
//
//    @Bean
//    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
//        // @formatter:off
//        http
//                // Demonstrate that method security works
//                // Best practice to use both for defense in depth
//                .authorizeExchange((authorize) -> authorize
//                        .anyExchange().permitAll()
//                )
//                .httpBasic(Customizer.withDefaults());
//        // @formatter:on
//        return http.build();
//    }
//
//    @Bean
//    MapReactiveUserDetailsService userDetailsService() {
//        // @formatter:off
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN", "USER")
//                .build();
//        // @formatter:on
//        return new MapReactiveUserDetailsService(user, admin);
//    }
//    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    public Advisor customAuthorize() {
//        JdkRegexpMethodPointcut pattern = new JdkRegexpMethodPointcut();
//        pattern.setPattern("org.mycompany.myapp.service.*");
//        ReactiveAuthorizationManager<MethodInvocation> rule = AuthorityAuthorizationManager.isAuthenticated();
//        AuthorizationManagerBeforeReactiveMethodInterceptor interceptor = new AuthorizationManagerBeforeReactiveMethodInterceptor(pattern, rule);
//        interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE_ADVISOR_ORDER.getOrder() + 1);
//        return interceptor;
//    }
}
