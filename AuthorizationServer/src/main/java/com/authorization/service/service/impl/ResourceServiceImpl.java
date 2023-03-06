package com.authorization.service.service.impl;


import com.authorization.service.entity.Authority;
import com.authorization.service.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author hjl
 * @date 2022/9/20 10:58
 */
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AuthorityRepository authorityRepository;

    //优化：应该各个服务读controller接口，缓存到redis，利用主从复制同步到网关服务器的redis中
    @PostConstruct
    public void initData() {
        List<Authority> all = authorityRepository.findAll();
        resourceRolesMap = new TreeMap<>();
        List<String> list = new ArrayList<>();
        list.add("ADMIN");
        resourceRolesMap.put("/api/hello", list);
        resourceRolesMap.put("/api/captcha", list);
        resourceRolesMap.put("/sys-comm/user/userInfo", list);
        resourceRolesMap.put("/sys-comm/sys/menu/nav", list);
//        resourceRolesMap.put("/sys-comm/sys/menu/list", list);
        resourceRolesMap.put("/sys-comm/sys/user/info", list);
        resourceRolesMap.put("/sys-comm/sys/role/info", list);
        resourceRolesMap.put("/sys-comm/sys/menu/info", list);
        resourceRolesMap.put("/sys-comm/sys/role/authList", list);

        resourceRolesMap.put("/sys-comm/sys/register/list", list);
        resourceRolesMap.put("/sys-comm/sys/register/save", list);
        resourceRolesMap.put("/sys-comm/sys/register/update", list);
        resourceRolesMap.put("/sys-comm/sys/register/info", list);
        resourceRolesMap.put("/sys-comm/sys/register/delete", list);

        resourceRolesMap.put("/sys-comm/sys/personnel/list", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/info", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/update", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/save", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/delete", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/pass", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/unPass", list);
        resourceRolesMap.put("/sys-comm/sys/personnel/doingRegister", list);

        resourceRolesMap.put("/sys-comm/sys/handbook/list", list);
        resourceRolesMap.put("/sys-comm/sys/handbook/info", list);
        resourceRolesMap.put("/sys-comm/sys/handbook/update", list);
        resourceRolesMap.put("/sys-comm/sys/handbook/save", list);
        resourceRolesMap.put("/sys-comm/sys/handbook/delete", list);
        all.stream().map(authority -> authority.getPerms()).collect(Collectors.toList()).forEach(permission -> {
            resourceRolesMap.put("/sys-comm/" + permission.replace(":", "/"), list);
        });
        list.clear();
        list.add("ADMIN");
        list.add("TEST");
        resourceRolesMap.put("/api/user/currentUser", list);
        redisTemplate.opsForHash().putAll("AUTH:RESOURCE_ROLES_MAP", resourceRolesMap);
    }
}
