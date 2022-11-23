package com.service.auth.controller;


import com.service.auth.entity.User;

import com.service.auth.repository.UserRepository;
import com.service.auth.service.LoginService;
import com.service.auth.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController//自定义登录接口
public class login {
    private LoginService loginService;

    @Autowired
    public void InitLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/login")
    public ResponseResult login(User user) {
        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }

    @GetMapping("/user/userInfo")
    public ResponseResult userInfo(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Map map = new HashMap();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
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
}
