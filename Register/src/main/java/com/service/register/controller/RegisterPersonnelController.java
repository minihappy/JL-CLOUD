package com.service.register.controller;

import com.service.register.entity.RegisterPersonnel;
import com.service.register.utils.RedisCache;
import com.service.register.utils.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjl
 * @since 2022-11-17
 */
@RestController
@RequestMapping("/register/registerPersonnel")
public class RegisterPersonnelController extends BaseController {
    @Resource
    RedisCache redisCache;

    @GetMapping("/getProcess/{email}")
    public Result sentProcessToEmail(@PathVariable("email") String email) {
        if (registerPersonnelServiceImpl.sentProcessToEmail(email)) {
            return Result.success("报名进度将通过邮箱发送");
        } else {
            return Result.fail("找不到相关报名信息");
        }
    }

    @PostMapping("/inRegister")
    public Result inRegister(@RequestBody RegisterPersonnel registerPersonnel) {

        //日志操作
        return Result.success(registerPersonnel);
    }

    @Transactional
    @PostMapping("/save")
    public Result save(@RequestBody RegisterPersonnel registerPersonnel) {
        registerPersonnel.setStatus(0);
        registerPersonnelServiceImpl.save(registerPersonnel);
        //日志操作
        return Result.success(registerPersonnel);
    }
}
