package com.service.source.contorller.sys;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.source.data.user.AuthorityRepository;
import com.service.source.data.user.RoleRepository;
import com.service.source.data.user.UserRepository;
import com.service.source.dto.RoleAuthorityDto;
import com.service.source.entity.Authority;
import com.service.source.entity.Role;
import com.service.source.service.RabbitMQServiceImpl;
import com.service.source.utils.RedisCache;
import com.service.source.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.service.source.dto.AuthorityDto.buildTreeMenu;


/**
 * <p>
 * 前端控制器
 * swagger2
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@CrossOrigin
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RabbitMQServiceImpl rabbitMQServiceImpl;

    //	@PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/info/{id}")
    public ResponseResult info(@PathVariable("id") Long id) {
        Optional<Role> roleOp = roleRepository.findById(id);
        return new ResponseResult(200, "成功", roleOp);
    }

    //    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/authList/{id}")
    public ResponseResult authList(@PathVariable("id") Long id) {
        List<RoleAuthorityDto> sysRole = roleRepository.findRoleById(id);
        List<RoleAuthorityDto> roleAuthorities = RoleAuthorityDto.setChildrenAndFatherToDto(sysRole);
        return new ResponseResult(200, "成功", roleAuthorities);
    }

    //    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/list")
    public ResponseResult list(int current, int size, String name) {
//        Sort sort = Sort.by(Sort.Direction.DESC, "id");
//        PageRequest of = PageRequest.of(1, 5, sort);
        PageRequest page = PageRequest.of(
                current - 1, size, Sort.by("id").descending());
        Page<Role> all;

        if (name == null || name.isEmpty())
            all = roleRepository.findAll(page);
        else
            all = roleRepository.findRoleByNameLike("%" + name + "%", page);
//        Iterable<Role> all1 = roleRepository.findAll();
        return new ResponseResult(200, "成功", all);
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('sys:role:save')")
    public ResponseResult save(@Validated @RequestBody Role sysRole) {

        roleRepository.save(sysRole);
        return new ResponseResult(200, "成功");
    }

    @PostMapping("/update")
//    @PreAuthorize("hasAuthority('sys:role:update')")
    public ResponseResult update(@Validated @RequestBody Role sysRole) {
        roleRepository.save(sysRole);
        return new ResponseResult(200, "成功");
    }

    @PostMapping("/delete")
//    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public ResponseResult info(@RequestBody Long[] ids) {
        roleRepository.deleteRoleAllRelation(ids);
        for (Long id : ids) {
            roleRepository.deleteById(id);
        }
        return new ResponseResult(200, "成功");
    }

    @SneakyThrows
    @Transactional
    @PostMapping("/perm/{roleId}")
//    @PreAuthorize("hasAuthority('sys:role:perm')")
    public ResponseResult info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        roleRepository.deleteRoleAuthRelation(roleId);
        Arrays.stream(menuIds).forEach(menuId -> {
            roleRepository.addRoleAuthRelation(roleId, menuId);
        });//使用消息队列，把用户权限的修改推送到中间件中，然后各个系统进行同步更新权限
        List<RoleAuthorityDto> roleById = roleRepository.findRoleById(roleId);
        List<Long> userIdByRoleId = userRepository.findUserIdByRoleId(roleId);
        String AfterChangeRoleValue = JSONObject.toJSONString(RoleAuthorityDto.setChildrenAndFatherToDto(roleById));
        userIdByRoleId.forEach(id -> {
            redisCache.setCacheMapValue("login:" + id, "role", JSONArray.parseArray(AfterChangeRoleValue));
        });
//        rabbitMQServiceImpl.sendMsgByTopicExchange(AfterChangeRoleValue, "change.role.all");
        return new ResponseResult(200, "成功");
    }

}
