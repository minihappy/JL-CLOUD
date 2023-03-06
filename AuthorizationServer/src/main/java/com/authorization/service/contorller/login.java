package com.authorization.service.contorller;


import com.authorization.service.entity.User;
import com.authorization.service.repository.UserRepository;
import com.authorization.service.service.LoginService;
import com.authorization.service.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Result login(User user) {

        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public Result logout() {
        return loginService.logout();
    }

    @GetMapping("/user/userInfo")
    public Result userInfo(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Map map = new HashMap();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        return Result.success(200, "用户信息", map);
    }
}
