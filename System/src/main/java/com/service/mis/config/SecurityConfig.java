package com.service.mis.config;

import com.service.mis.data.user.UserDetailsServiceImpl;
import com.service.mis.filter.CaptchaFilter;
import com.service.mis.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


import javax.sql.DataSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private DataSource dataSource;
    private UserDetailsServiceImpl UserDetailsServiceImpl;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public void enableDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void enableUserRepository(UserDetailsServiceImpl UserDetailsServiceImpl) {
        this.UserDetailsServiceImpl = UserDetailsServiceImpl;
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    CaptchaFilter captchaFilter;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("123").roles("user");
        auth.userDetailsService(UserDetailsServiceImpl).passwordEncoder(bcryptEncoder());
    }

    private static final String[] URL_WHITELIST = {
            "/user/login",
            "/user/logout",
            "/captcha",
            "/favicon.ico",

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http                //??????csrf
                .csrf().disable()
                //?????????Session??????SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // ??????????????????,?????????????????? ??????????????????
                .antMatchers(URL_WHITELIST).anonymous()
                // ???????????????????????????????????????????????????
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(captchaFilter, JwtAuthenticationTokenFilter.class);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);
        // remember-me
        http.rememberMe()
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(86400);

//         ??????????????????????????????csrf?????? ???????????????
//        http.csrf().ignoringAntMatchers("/h2-console/**");
        // ?????????????????? iframe ???????????????
        http.headers().frameOptions().sameOrigin();
        //????????????
        http.cors();
    }

    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository =
                new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true);

        return tokenRepository;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
