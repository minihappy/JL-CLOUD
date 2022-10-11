package com.service.mis.service.impl;

import com.service.mis.entity.User;
import com.service.mis.service.LoginService;
import com.service.mis.utils.JwtUtil;
import com.service.mis.utils.RedisCache;
import com.service.mis.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {


    private AuthenticationManager authenticationManager;

    @Autowired
    public void initAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private RedisCache redisCache;

    @Autowired
    public void initRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager会调用Authentication 一步一步向下查找，loadByUserName方法，然后从自定义的UserDetails中根据用户名查找这个用户的权限
        //也就是说自定义的AuthenticationManager负责匹配账号密码是否正确，自定义的UserDetails负责获取权限，再返回给调用者
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        User loginUser = (User) authenticate.getPrincipal();
        String userId = loginUser.getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    public ResponseResult logout() {//退出不带任何参数，从同一个请求里面获取身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//从Security中获取身份
        User loginUser = (User) authentication.getPrincipal();//根据身份获取User实体
        Long id = loginUser.getId();//获取当前的id
        redisCache.deleteObject("login:" + id);//将id从redis中删除，，这样JWT过滤器在过滤请求的时候就会找不到redis中的登录信息，从而判断登录失败
        return new ResponseResult(200, "退出成功");//这里不用手动删除SecurityContextHolder中的authentication,原因是每个请求都会对应一个authentication，后面的新请求自然对不上
    }
}
