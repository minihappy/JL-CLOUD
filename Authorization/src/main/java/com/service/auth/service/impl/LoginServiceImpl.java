package com.service.auth.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.service.auth.entity.User;
import com.service.auth.service.LoginService;
import com.service.auth.utils.RedisCache;
import com.service.auth.utils.ResponseResult;
import com.service.auth.utils.JwtUtil;
import lombok.SneakyThrows;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private KeyPair keyPair;
    @Autowired
    private RSAKey rsaKey;
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

    @SneakyThrows
    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager会调用Authentication 一步一步向下查找，loadByUserName方法，然后从自定义的UserDetails中根据用户名查找这个用户的权限
        //也就是说自定义的AuthenticationManager负责匹配账号密码是否正确，自定义的UserDetails负责获取权限，再返回给调用者
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);//调用自定义的AuthenticationProvider
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        User loginUser = (User) authenticate.getPrincipal();
        String userId = loginUser.getId().toString();
        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("id", userId);
        customClaims.put("authorities", loginUser.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()));
        customClaims.put("user_name", user.getUsername());
        List list = new ArrayList();
        list.add("all");
        customClaims.put("scope", list);
        customClaims.put("client_id", "client-app");
        String jwt = jwtUtil.createJWT("hjl", customClaims, keyPair, rsaKey);
        System.out.println(jwt);
//        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());
//        JWTClaimsSet jwtClaimsSet;
//        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
//                .subject("ggg")
//                .issuer("http://127.0.0.1:9000")
//                .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 10))
//                .issueTime(new Date(new Date().getTime()))
//                .jwtID(rsaKey.getKeyID());
//        List jsonArray = new ArrayList();
//        jsonArray.add("ADMIN");
//        List jsonArray1 = new ArrayList();
//        jsonArray1.add("all");
//        claims.claim("user_name", "admin");
//        claims.claim("authorities", jsonArray);
//        claims.claim("scope", jsonArray1);
//        claims.claim("client_id", "client-app");
//        claims.claim("id", 1);
//        jwtClaimsSet = claims.build();
//        SignedJWT signedJWT = new SignedJWT(
//                new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
//                jwtClaimsSet);
//        signedJWT.sign(signer);
//        String jwt = signedJWT.serialize();
//        System.out.println(jwt);
//        signedJWT = SignedJWT.parse(jwt);
//        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) keyPair.getPublic());
//        System.out.println(keyPair.getPublic());
//        System.out.println(signedJWT.verify(verifier));
        //authenticate存入redis
        Map<String, Object> map1 = JSONObject.parseObject(JSONObject.toJSONString(loginUser), Map.class);
        redisCache.setCacheMap("login:" + userId, map1);
//        redisCache.setCacheObject("login:" + userId, loginUser);
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
