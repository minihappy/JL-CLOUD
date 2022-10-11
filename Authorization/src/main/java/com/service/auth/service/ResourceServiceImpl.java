package com.service.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hjl
 * @date 2022/9/20 10:58
 */
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initData() {
        resourceRolesMap = new TreeMap<>();
        List<String> list = new ArrayList<>();
        list.add("ADMIN");
        resourceRolesMap.put("/api/hello", list);
        resourceRolesMap.put("/api/captcha", list);
        list.clear();
        list.add("ADMIN");
        list.add("TEST");
        resourceRolesMap.put("/api/user/currentUser", list);
        resourceRolesMap.put("/RegisterEnumKey/hello", list);
        redisTemplate.opsForHash().putAll("AUTH:RESOURCE_ROLES_MAP", resourceRolesMap);
    }
}
