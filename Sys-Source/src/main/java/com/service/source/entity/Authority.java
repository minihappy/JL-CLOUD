package com.service.source.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;


import javax.persistence.*;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


//@Entity
//@JsonIgnoreProperties(value = { "authority" })
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "parentId")
    private long parentId;
    private String name;
    private String path;
    private String perms;
    private String component;
    private int type;
    private String icon;
    private int orderNum;
//    private Date created;

//    @PrePersist
//    void setCreated() {
//        this.created = new Date();
//    }

    //    private Date updated;
    private int status;
    @Transient
    private List<Authority> children = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)//懒加载   快速查询 不会查询role表
//    @JoinTable(
//            name = "role_authority",
//            joinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    @JoinTable(name = "role_authority",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<Role> role;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;
//    public Authority() {
//
//    }
//
//    public Authority(long id, long parentId, String name, String path, String perms, String component, int type, String icon, int orderNum, Date created, Date updated, int status, List<Authority> children, List<Role> role) {
//        this.id = id;
//        this.parentId = parentId;
//        this.name = name;
//        this.path = path;
//        this.perms = perms;
//        this.component = component;
//        this.type = type;
//        this.icon = icon;
//        this.orderNum = orderNum;
//        this.created = created;
//        this.updated = updated;
//        this.status = status;
//        this.children = children;
//        this.role = role;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getParentId() {
//        return parentId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public String getPerms() {
//        return perms;
//    }
//
//    public String getComponent() {
//        return component;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public int getOrderNum() {
//        return orderNum;
//    }
//
//    public Date getCreated() {
//        return created;
//    }
//
//    public void setCreated(Date created) {
//        this.created = created;
//    }
//
//    public Date getUpdated() {
//        return updated;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public List<Authority> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<Authority> children) {
//        this.children = children;
//    }
//
//    public List<Role> getRole() {
//        return role;
//    }
//
//    public void setRole(List<Role> role) {
//        this.role = role;
//    }
}
