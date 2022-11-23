package com.service.source.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleAuthorityDto implements Serializable {
    private long id;
    private String component;
    private String icon;
    private String name;
    private int orderNum;
    private int parentId;
    private String path;
    private String perms;
    private int status;
    private int type;
    private Integer authorityId;
    private List<RoleAuthorityDto> children = new ArrayList<>();

    public static List<RoleAuthorityDto> RoleAuthorityToDto(List<RoleAuthorityDto> authority) {
        List<RoleAuthorityDto> list = new ArrayList<>();
        authority.forEach(m -> {
            RoleAuthorityDto dto = new RoleAuthorityDto();
            dto.setId(m.getId());
            dto.setComponent(m.getComponent());
            dto.setIcon(m.getIcon());
            dto.setName(m.getName());
            dto.setOrderNum(m.getOrderNum());
            dto.setParentId(m.getParentId());
            dto.setPath(m.getPath());
            dto.setStatus(m.getStatus());
            dto.setType(m.getType());
            dto.setAuthorityId(m.getAuthorityId());
            ;
            if (m.getChildren().size() > 0) {
                // 子节点调用当前方法进行再次转换
                dto.setChildren(RoleAuthorityToDto(m.getChildren()));
            }
            list.add(dto);
        });
        return list;
    }

    public static List<RoleAuthorityDto> setChildrenAndFatherToDto(List<RoleAuthorityDto> role_authority) {
        List<RoleAuthorityDto> parentAuthority = buildTreeMenu(role_authority);
        role_authority.forEach(rs -> {
            List<RoleAuthorityDto> authority = rs.getChildren();
            authority.forEach(as -> {
                authority.forEach(as1 -> {//这里再次遍历authority是为了遍历出子孩子
                    if (as1.getParentId() == as.getId()) {//如果父节点等于当前遍历节点,,添加节点为当前遍历节点的孩子
                        as.getChildren().add(as1);
                    }
                });
                if (as.getParentId() == 0) {//如果没有父节点，本身就是父节点
                    parentAuthority.add(as);
                }
            });
        });
        return RoleAuthorityToDto(parentAuthority);
    }

    public static List<RoleAuthorityDto> buildTreeMenu(List<RoleAuthorityDto> menus) {

        List<RoleAuthorityDto> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (RoleAuthorityDto menu : menus) {

            for (RoleAuthorityDto e : menus) {
                if (menu.getId() == e.getParentId()) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }
}
