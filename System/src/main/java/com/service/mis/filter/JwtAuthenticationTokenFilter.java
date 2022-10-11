package com.service.mis.filter;

import com.service.mis.entity.User;
import com.service.mis.handler.AuthenticationEntryPointImpl;
import com.service.mis.utils.JwtUtil;
import com.service.mis.utils.RedisCache;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPointImpl;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");//前端将携带jwt请求过滤器
        if (StringUtils.hasText(token)) {//如果是包含token就继续判断身份，否则放行
            //解析token
            String id;
            try {
                Claims claims = JwtUtil.parseJWT(token);//获取请求头的JWT
                id = claims.getSubject();//解析JWT的id
            } catch (Exception e) {
                e.printStackTrace();
                authenticationEntryPointImpl.setMsg("非法的Token");
                throw new RuntimeException("非法的Token");
            }
            //从redis中获取用户信息
            String redisKey = "login:" + id;
            User loginUser = redisCache.getCacheObject(redisKey);//根据登录的id去redis中查找是否存在这个数据
            if (Objects.isNull(loginUser)) {
                authenticationEntryPointImpl.setMsg("用户未登录");
                throw new RuntimeException("用户未登录");
            }
            //存入SecurityContextHolder
            //TODO 获取权限信息封装到Authentication中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());//使用user这个类是为了封装authenticationToken,并设置authentication为认证状态
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);//将authentication存入SecurityContextHolder,以便提供其它过滤器使用
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
