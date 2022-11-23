package com.service.source.dto;

import com.service.source.entity.Authority;
import com.service.source.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AuthorityDto implements Serializable {
    private long id;
    private long parentId;
    private String title;
    private String path;
    private String name;
    private String perms;
    private String component;
    private int type;
    private String icon;
    private int orderNum;
    private int status;
    private String createBy;
    private String changeBy;
    private Date createTime;
    private Date changeTime;
    private List<AuthorityDto> children = new ArrayList<>();
//    public AuthorityDto() {
//
//    }
//
//    public AuthorityDto(Long id, String name, String title, String icon, String path, String component, List<AuthorityDto> children) {
//        this.id = id;
//        this.name = name;
//        this.title = title;
//        this.icon = icon;
//        this.path = path;
//        this.component = component;
//        this.children = children;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getComponent() {
//        return component;
//    }
//
//    public void setComponent(String component) {
//        this.component = component;
//    }
//
//    public List<AuthorityDto> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<AuthorityDto> children) {
//        this.children = children;
//    }

    public static List<AuthorityDto> AuthorityToDto(List<Authority> authority) {
        List<AuthorityDto> list = new ArrayList<>();
        authority.forEach(m -> {
            AuthorityDto dto = new AuthorityDto();
            dto.setId(m.getId());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            dto.setParentId(m.getParentId());
            dto.setPerms(m.getPerms());
            dto.setName(m.getName());
            dto.setType(m.getType());
            dto.setIcon(m.getIcon());
            dto.setOrderNum(m.getOrderNum());
            dto.setStatus(m.getStatus());
            dto.setCreateBy(m.getCreateBy());
            dto.setChangeBy(m.getChangeBy());
            dto.setCreateTime(m.getCreateTime());
            dto.setChangeTime(m.getChangeTime());
            if (m.getChildren().size() > 0) {
                // 子节点调用当前方法进行再次转换
                dto.setChildren(AuthorityToDto(m.getChildren()));
            }
            list.add(dto);
        });
        return list;
    }

    public static List<AuthorityDto> setChildrenAndFatherToDto(List<Role> role) {
        List<Authority> parentAuthority = new ArrayList<>();
        role.forEach(rs -> {
            List<Authority> authority = rs.getAuthority();
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
        return AuthorityToDto(parentAuthority);
//        Iterator<Role> iterator = role.iterator();
//        while (iterator.hasNext()) {
//            Role next = iterator.next();
//            List<Authority> authorityList = next.getAuthority();
//            Iterator<Authority> iterator1 = authorityList.iterator();
//            while (iterator1.hasNext()) {
//                Authority next1 = iterator1.next();
//                for (Authority a : authorityList) {
//                    if (next1.getId() == a.getParent_id()) {
//                        next1.getChildren().add(a);
//                    }
//                }
//
//            }
//        }
    }

    public static List<Authority> buildTreeMenu(List<Authority> menus) {

        List<Authority> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (Authority menu : menus) {

            for (Authority e : menus) {
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
