package com.service.mis.contorller.sys;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.mis.data.user.AuthorityRepository;
import com.service.mis.data.user.RoleRepository;
import com.service.mis.data.user.UserRepository;
import com.service.mis.dto.RoleAuthorityDto;
import com.service.mis.entity.Authority;
import com.service.mis.entity.Role;
import com.service.mis.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.service.mis.dto.AuthorityDto.buildTreeMenu;


/**
 * <p>
 * 前端控制器
 * swagger2
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;


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
        int i = roleRepository.deleteRoleAllRelation(ids);
        if (i == ids.length) {
            for (Long id : ids) {
                roleRepository.deleteById(id);
            }
        }
        return new ResponseResult(200, "成功");
    }

    @Transactional
    @PostMapping("/perm/{roleId}")
//    @PreAuthorize("hasAuthority('sys:role:perm')")
    public ResponseResult info(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        roleRepository.deleteRoleAuthRelation(roleId);
        Arrays.stream(menuIds).forEach(menuId -> {
            roleRepository.addRoleAuthRelation(roleId, menuId);
        });
        return new ResponseResult(200, "成功");
    }

}
