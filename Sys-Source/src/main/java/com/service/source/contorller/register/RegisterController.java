package com.service.source.contorller.register;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.service.source.data.register.RegisterRepository;
import com.service.source.data.user.UserRepository;
import com.service.source.dto.RegisterDao;
import com.service.source.entity.Authority;
import com.service.source.entity.User;
import com.service.source.entity.register.Register;
import com.service.source.utils.RedisCache;
import com.service.source.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.service.source.dto.AuthorityDto.AuthorityToDto;


/**
 * @author hjl
 * @description
 * @date 2022/11/24 13:24
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/register")
public class RegisterController {
    @Resource
    RegisterRepository registerRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    private RedisCache redisCache;

    @GetMapping("/list")
    public ResponseResult getRegisterList(int current, int size, String username) {
        PageRequest pageRequest = PageRequest.of(current - 1, size, Sort.by("id").descending());
        Iterable<Register> all;
        if (username == null || username.isEmpty()) {
            all = registerRepository.findAll(pageRequest);
        } else {
            all = registerRepository.findRegisterByNameLike(username, pageRequest);
        }
        return new ResponseResult(200, "成功", all);
    }

    @GetMapping("/doingRegister")
    public ResponseResult getDoingRegister() {
        List<RegisterDao> list = registerRepository.findDoingRegister();
        return new ResponseResult(200, "成功", list);
    }

    @GetMapping("/info/{id}")
    public ResponseResult getRegisterInfo(@PathVariable Long id) {
        if (id > 0) {
            Optional<Register> all = registerRepository.findById(id);
            return new ResponseResult(200, "成功", all);
        } else {
            Iterable<User> all = userRepository.findAll();
            JSONArray jsonArray = new JSONArray();
            all.forEach(user -> {
                jsonArray.add(user);
            });
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("users", jsonArray);
            return new ResponseResult(200, "成功", jsonObject);
        }
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('sys:menu:save')")
    public ResponseResult save(@Validated @RequestBody Register register, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;

        register.setUpdateMan(user.get("username").toString());
        Register save = registerRepository.save(register);
        List<Register> list = new ArrayList<>();
        list.add(save);
        return new ResponseResult(200, "成功", list);
    }

    @PostMapping("/update")
//    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ResponseResult update(@Validated @RequestBody Register register, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;
        register.setUpdateMan(user.get("username").toString());
        Register save = registerRepository.save(register);
        List<Register> list = new ArrayList<>();
        list.add(save);
        // 清除所有与该菜单相关的权限缓存
//        authorityRepository.deleteAuthorityById(sysMenu.getId());
        return new ResponseResult(200, "成功", list);
    }

    @SneakyThrows
    @GetMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") Long id) {
        registerRepository.deleteById(id);
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
