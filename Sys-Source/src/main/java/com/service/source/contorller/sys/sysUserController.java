package com.service.source.contorller.sys;

import com.service.source.data.user.RoleRepository;
import com.service.source.data.user.UserRepository;
import com.service.source.dto.UserDto;
import com.service.source.entity.User;
import com.service.source.utils.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/sys/user")
public class sysUserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/info/{id}")
//    @PreAuthorize("hasAuthority('sys:user:list')")
    public ResponseResult info(@PathVariable("id") Long id) {
        Optional<User> byId = userRepository.findById(id);
//        User user = byId.get();

        return new ResponseResult(200, "成功", byId);
    }

    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('sys:user:list')")
    public ResponseResult list(int current, int size, String username) {
        PageRequest pageRequest = PageRequest.of(current - 1, size, Sort.by("id").descending());
        Page<User> all;
        if (username == null || username.isEmpty())
            all = userRepository.findAll(pageRequest);
        else
            all = userRepository.findUserByUsernameLike("%" + username + "%", pageRequest);
        all.map(view -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(view, userDto);
            return userDto;
        });
        return new ResponseResult(200, "成功", all);
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('sys:user:save')")
    public ResponseResult save(@Validated @RequestBody User sysUser) {
        sysUser.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(sysUser);
        return new ResponseResult(200, "成功");
    }

    @PostMapping("/update")
//    @PreAuthorize("hasAuthority('sys:user:update')")
    public ResponseResult update(@Validated @RequestBody User sysUser) {
        userRepository.save(sysUser);
        return new ResponseResult(200, "成功");
    }

    @Transactional
    @PostMapping("/delete")
//    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ResponseResult delete(@RequestBody Long[] ids) {
        List<Long> longs = Arrays.asList(ids);
        int i = userRepository.deleteUserByIds(longs);

        return new ResponseResult(200, "成功");
    }

    @Transactional
    @PostMapping("/role/{userId}")
//    @PreAuthorize("hasAuthority('sys:user:role')")
    public ResponseResult rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        userRepository.deleteUserRoleRelationByUserId(userId);
        int i = userRepository.saveUserRoleRelation(userId, roleIds);
        if (i > 0)
            return new ResponseResult(200, "成功");
        else
            return new ResponseResult(202, "失败");
    }

    @PostMapping("/rePass")
//    @PreAuthorize("hasAuthority('sys:user:repass')")
    public ResponseResult rePassWord(Long userId) {
        String newPass = "123456";
        userRepository.changePassWord(userId, passwordEncoder.encode(newPass));
        return new ResponseResult(200, "成功");
    }

//    @PostMapping("/updatePass")
//    public ResponseResult updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {
//
//        return new ResponseResult(200, "成功");
//    }
}
