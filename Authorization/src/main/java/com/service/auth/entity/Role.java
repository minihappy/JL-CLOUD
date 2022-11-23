package com.service.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
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
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<User> user;
    //急加载 会查询role表
//    @JsonIgnore
    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<Authority> authority;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;

    public List<User> getUser() {
        user.forEach(u -> {
            u.setRole(new ArrayList<>());
        });
        return user;
    }

    public List<Authority> getAuthority() {
        authority.forEach(a -> {
            a.setRole(new ArrayList<>());
        });
        return authority;
    }
}
