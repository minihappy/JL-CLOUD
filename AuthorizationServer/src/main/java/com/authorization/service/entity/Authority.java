package com.authorization.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @ManyToMany(fetch = FetchType.EAGER)//懒加载   快速查询 不会查询role表
//    @JoinTable(
//            name = "role_authority",
//            joinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JoinTable(name = "role_authority", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Role> role;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;

    public List<Role> getRole() {//为什么要手写getRole；因为做数据处理时@JsonIgnore不会有效的过滤双向数据依赖
        role.forEach(r -> {//在authority类里面获取角色时，如果authority主动去获取role就设置空，否则会无限嵌套堆栈溢出
            r.setAuthority(new ArrayList<>());
        });
        return role;
    }
}
