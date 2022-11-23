package com.service.source.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//@Entity
//@JsonIgnoreProperties(value = { "role" })
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //    @NotBlank(message = "角色名称不能为空")
    private String name;

    //    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
     * 备注
     */
    private String remark;
    //懒加载 不会查询role表
//    @JsonIgnore
//    @ManyToMany(mappedBy = "role",fetch = FetchType.LAZY)
//    private List<User> user;
    @JsonIgnore//查找role时排除user结果
    @Fetch(FetchMode.SUBSELECT)
    //mappedBy:以下内容为，多对多模式role表为维护端。
    //为什么呢：因为多对多需要有一端放弃维护，否则无限嵌套
    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
//    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<User> user;
    //急加载 会查询role表z
//    @JsonIgnore
    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
//    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<Authority> authority;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;
//
//    public Role(long id, String name, List<User> user, List<Authority> authority) {
//        this.id = id;
//        this.name = name;
//        this.user = user;
//        this.authority = authority;
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
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public List<User> getUser() {
//        return user;
//    }
//
//    public void setUser(List<User> user) {
//        this.user = user;
//    }
//
//    public List<Authority> getAuthority() {
//        return authority;
//    }
//
//    public void setAuthority(List<Authority> authority) {
//        this.authority = authority;
//    }
//
//    public Role() {
//
//    }
}
