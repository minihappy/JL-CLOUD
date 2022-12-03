package com.service.source.contorller.register;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.service.source.data.register.RegisterPersonnelRepository;
import com.service.source.data.register.RegisterRepository;
import com.service.source.data.user.UserRepository;
import com.service.source.entity.User;
import com.service.source.entity.register.Register;
import com.service.source.entity.register.RegisterPersonnel;
import com.service.source.utils.RedisCache;
import com.service.source.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author hjl
 * @description
 * @date 2022/11/28 15:44
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/personnel")
public class RegisterPersonnelController {

    @Resource
    RegisterPersonnelRepository registerPersonnelRepository;
    @Resource
    RegisterRepository registerRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    private RedisCache redisCache;

    @GetMapping("/list")
    public ResponseResult getRegisterList(int current, int size, String name) {
        PageRequest pageRequest = PageRequest.of(current - 1, size, Sort.by("id").descending());
        Iterable<RegisterPersonnel> all;
        if (name == null || name.isEmpty()) {
            all = registerPersonnelRepository.findAll(pageRequest);
        } else {
            all = registerPersonnelRepository.findRegisterPersonnelByNameLike(name, pageRequest);
        }
        return new ResponseResult(200, "成功", all);
    }

    @GetMapping("/info/{id}")
    public ResponseResult getRegisterInfo(@PathVariable Long id) {
        if (id > 0) {
            Optional<RegisterPersonnel> all = registerPersonnelRepository.findById(id);
            return new ResponseResult(200, "成功", all);
        } else {
            Iterable<Register> register = registerRepository.findAll();
            Iterable<User> all = userRepository.findAll();
            JSONArray jsonArray = new JSONArray();
            all.forEach(user -> {
                jsonArray.add(user);
            });
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("users", jsonArray);
            jsonObject.put("registers", register);
            return new ResponseResult(200, "成功", jsonObject);
        }
    }

    @PostMapping("/save/{registerId}")
//    @PreAuthorize("hasAuthority('sys:menu:save')")
    public ResponseResult save(@Validated @RequestBody RegisterPersonnel register, @PathVariable("registerId") Long registerId, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;
        register.setRegister(registerRepository.findById(registerId).get());//根据前端的id查询报名保存
        register.setCheck_man(user.get("username").toString());
        RegisterPersonnel save = registerPersonnelRepository.save(register);
        List<RegisterPersonnel> list = new ArrayList<>();
        list.add(save);
        return new ResponseResult(200, "成功", list);
    }

    @PostMapping("/update/{registerId}")
//    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ResponseResult update(@Validated @RequestBody RegisterPersonnel register, @PathVariable("registerId") Long registerId, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;
        register.setRegister(registerRepository.findById(registerId).get());//根据前端的id查询报名保存
        register.setCheck_man(user.get("username").toString());
        RegisterPersonnel save = registerPersonnelRepository.save(register);
        List<RegisterPersonnel> list = new ArrayList<>();
        list.add(save);
        // 清除所有与该菜单相关的权限缓存
//        authorityRepository.deleteAuthorityById(sysMenu.getId());
        return new ResponseResult(200, "成功", list);
    }

    @SneakyThrows
    @GetMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") Long[] ids) {
        for (Long id : ids) {
            registerPersonnelRepository.deleteById(id);
        }
        return new ResponseResult(200, "操作成功");
    }

    @SneakyThrows
    @GetMapping("/pass/{id}")
    public ResponseResult Pass(@PathVariable("id") List<Long> id) {
        registerPersonnelRepository.Pass(id);
        return new ResponseResult(200, "操作成功");
    }

    @SneakyThrows
    @GetMapping("/unPass/{id}")
    public ResponseResult unPass(@PathVariable("id") List<Long> id) {
        registerPersonnelRepository.unPass(id);
        return new ResponseResult(200, "操作成功");
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
