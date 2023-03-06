package com.authorization.service.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private int status;
    private String email;
    private String username;
    /*    @CreatedDate
        private Date created;*/
//    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<Role> role;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;

    @Column(length = 60)
    private String password;


    private boolean enabled = true;
    private boolean accountNonExpired = true;//账号未过期
    private boolean accountNonLocked = true;//账号未锁定
    private boolean credentialsNonExpired = true;//凭证未过期

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> role = this.getRole();
        List<GrantedAuthority> authorities = null;
        int index = role.size();
        List<String> list = new ArrayList();
        while (index > 0) {
            index--;
            list.add(role.get(index).getCode());
            authorities = list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ADMIN");//没有自定义遍历查询的时候才用这个方法查询返回
//        return Collections.singletonList(simpleGrantedAuthority);
        return authorities;
    }

    public List<Role> getRole() {//为什么要手写getRole；因为做数据处理时@JsonIgnore不会有效的过滤双向数据依赖
        role.forEach(r -> {//在user类里面获取角色时，如果role主动去获取user就设置空，否则会无限嵌套堆栈溢出
            r.setUser(new ArrayList<>());
        });
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
}
