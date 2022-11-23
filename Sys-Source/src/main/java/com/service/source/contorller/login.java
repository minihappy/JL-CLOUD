package com.service.source.contorller;


import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.service.source.data.user.UserRepository;
import com.service.source.entity.User;
import com.service.source.utils.RedisCache;
import com.service.source.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController//自定义登录接口
public class login {
    private UserRepository userRepository;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/user/userInfo")
    public ResponseResult userInfo(HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;
        Map map = new HashMap();
        map.put("id", user.get("id"));
        map.put("username", user.get("username"));
        return new ResponseResult(200, "用户信息", map);
//        SysUser sysUser = sysUserService.getByUsername(principal.getName());
//
//        return Result.succ(MapUtil.builder()
//                .put("id", sysUser.getId())
//                .put("username", sysUser.getUsername())
//                .put("avatar", sysUser.getAvatar())
//                .put("created", sysUser.getCreated())
//                .map()
//        );
//    }
    }

    @SneakyThrows
    private Object getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        SignedJWT parse = SignedJWT.parse(token);
        JWTClaimsSet jwtClaimsSet = parse.getJWTClaimsSet();
        Object claim = jwtClaimsSet.getClaim("id");
        String id = null;
        if (!(claim instanceof String)) {
            return new ResponseResult(200, "没有该用户");
        }
        id = claim.toString();
        String redisKey = "login:" + id;
        Object cacheObject1 = redisCache.getCacheMap(redisKey);
        if (!(cacheObject1 instanceof LinkedHashMap)) {
            return new ResponseResult(200, "用户信息不完整");
        }
        return cacheObject1;
    }
}
