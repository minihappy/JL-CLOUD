package com.authorization.service.service.impl;


import com.authorization.service.entity.Authority;
import com.authorization.service.entity.User;
import com.authorization.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(11);
//    }
    @Override
    public UserDetails loadUserByUsername(String userParam) throws UsernameNotFoundException {//重写userDetails的登录
        User user = userRepository.findByUsername(userParam);

        if (user == null) {//找不到用户就抛出异常
            throw new UsernameNotFoundException("账号密码错误");
        }
        String password = user.getPassword();
        Boolean enabled = user.isEnabled();
        Boolean accountNonExpired = user.isAccountNonExpired();
        Boolean credentialsNonExpired = user.isCredentialsNonExpired();
        Boolean accountNonLocked = user.isAccountNonLocked();
        List<GrantedAuthority> authorities = new ArrayList<>();

        Collection<? extends GrantedAuthority> authoritiesList = user.getAuthorities();
        List<String> collect = authoritiesList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        for (String auth : collect) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(auth);
            authorities.add(authority);
        }
        return new org.springframework.security.core.userdetails.User(userParam, password, enabled != null && enabled, accountNonExpired != null && accountNonExpired,
                credentialsNonExpired != null && credentialsNonExpired, accountNonLocked != null && accountNonLocked,
                authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {//根据角色获取权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
