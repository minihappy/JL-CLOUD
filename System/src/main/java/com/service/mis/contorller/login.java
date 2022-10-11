package com.service.mis.contorller;


import com.service.mis.data.user.UserRepository;
import com.service.mis.entity.User;
import com.service.mis.service.LoginService;
import com.service.mis.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseResult login(@RequestBody User user) {
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
