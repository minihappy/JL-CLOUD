package com.service.mis.dto;

import com.service.mis.entity.Role;
import com.service.mis.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private int status;
    private List<Role> role;
    private String createBy;
    private String changeBy;
    private Date createTime;
    private Date changeTime;

    public UserDto toUserDto(User user) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setPhone(user.getPhone());
        this.setStatus(user.getStatus());
        this.setRole(user.getRole());
        this.setCreateBy(user.getCreateBy());
        this.setChangeBy(user.getChangeBy());
        this.setCreateTime(user.getCreateTime());
        this.setChangeTime(user.getChangeTime());
        return this;
    }
}

