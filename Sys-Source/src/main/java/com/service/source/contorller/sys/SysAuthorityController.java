package com.service.source.contorller.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.service.source.data.user.AuthorityRepository;
import com.service.source.data.user.UserRepository;
import com.service.source.dto.AuthorityDto;
import com.service.source.entity.Authority;
import com.service.source.entity.Role;
import com.service.source.entity.User;
import com.service.source.utils.RedisCache;
import com.service.source.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.service.source.dto.AuthorityDto.*;
import static com.service.source.dto.AuthorityDto.AuthorityToDto;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@RestController
@RequestMapping("/sys/menu")
public class SysAuthorityController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RedisCache redisCache;

    /**
     * 用户当前用户的菜单和权限信息
     *
     * @return
     */

    @SneakyThrows
    @GetMapping("/nav")
    public ResponseResult nav(HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap linkedHashMap = (LinkedHashMap) object;
        List<Role> roles = JSON.parseObject(JSON.toJSONString(linkedHashMap.get("role")), new TypeReference<List<Role>>() {//使用fastJson反序列化LinkedHashMap转实体
        });
        Map map = new HashMap();
        map.put("nav", setChildrenAndFatherToDto(roles));
        Set<String> authSet = new HashSet<>();
        roles.forEach(role -> {
            role.getAuthority().forEach(auth -> {
                authSet.add(auth.getPerms());
            });
        });
        map.put("authorities", authSet);
        return new ResponseResult(200, "信息", map);
    }

    @GetMapping("/info/{id}")
//    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ResponseResult info(@PathVariable(name = "id") Long id) {
        Authority authority = authorityRepository.findAuthorityById(id);
        return new ResponseResult(200, "成功", authority);
    }

    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('sys:menu:list')")
    public ResponseResult list(Principal principal) {//根据权限，设置父子节点hjl20220507
        Iterable<Authority> all = authorityRepository.findAll();
        List<Authority> list = new ArrayList<>();
        all.forEach(a -> {
            list.add(a);
        });
        return new ResponseResult(200, "成功", AuthorityToDto(buildTreeMenu(list)));
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('sys:menu:save')")
    public ResponseResult save(@Validated @RequestBody Authority authority, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;

        authority.setCreateBy(user.get("username").toString());
        Authority save = authorityRepository.save(authority);
        List<Authority> list = new ArrayList<>();
        list.add(save);
        return new ResponseResult(200, "成功", list);
    }

    @PostMapping("/update")
//    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ResponseResult update(@Validated @RequestBody Authority sysMenu, HttpServletRequest request) {
        Object object = getUserInfo(request);
        if (object instanceof ResponseResult) {
            return (ResponseResult) object;
        }
        LinkedHashMap user = (LinkedHashMap) object;
        sysMenu.setChangeBy(user.get("username").toString());
        Authority save = authorityRepository.save(sysMenu);
        List<Authority> list = new ArrayList<>();
        list.add(save);
        // 清除所有与该菜单相关的权限缓存
//        authorityRepository.deleteAuthorityById(sysMenu.getId());
        return new ResponseResult(200, "成功", AuthorityToDto(list));
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('sys:menu:delete')")
//    public ResponseResult delete(Principal principal, @PathVariable("id") Long id) {
    public ResponseResult delete(@PathVariable("id") Long id) {
        long count = authorityRepository.countAuthorityByParentId(id);
        if (count > 0) {
            return new ResponseResult(202, "请先删除子菜单");
        }
//        // 清除所有与该菜单相关的权限缓存
//        userRepository.deleteUserAuthorityByAuthorityId(id);
//        User user = userRepository.findUserById(id);
//        List<Role> role = user.getRole();
//        for (int i = 0; i < role.size(); i++) {
//            count += authorityRepository.deleteRoleAndAuthority(role.get(i).getId(), id);
//        }
        authorityRepository.deleteById(id);
        if (count > 0) {
//            authorityRepository.deleteById(id);
        } else {
            return new ResponseResult(202, "删除用户和权限失败，程序停止");
        }
//        // 同步删除中间关联表
//        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
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
