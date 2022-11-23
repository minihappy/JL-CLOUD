package com.service.mis.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.security.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


//@Entity
//@JsonIgnoreProperties(value = { "user" })
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

//    private static final long serialVersionUID = -1681695020636685631L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private int status;
    /*    @CreatedDate
        private Date created;*/
//    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> role;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;

    //    public void addRole(Role role) {
//        this.roles.add(role);
//    }
//    @Transient
//    private User users;
//    @Transient
//    public User InitUser(User user){
//        this.users = user;
//        return user;
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> role = this.getRole();
        List<GrantedAuthority> authorities = null;
        int index = role.size();
        List<String> list = new ArrayList();
        while (index > 0) {
            index--;
            list.add(role.get(index).getName());
            authorities = list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ADMIN");//没有自定义遍历查询的时候才用这个方法查询返回
//        return Collections.singletonList(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public Date getCreated() {
//        return created;
//    }
//
//    public List<Role> getRole() {
//        return role;
//    }
//
//    public void setRole(List<Role> role) {
//        this.role = role;
//    }
//
//    public User(Long id, String username, String password, String email, String phone, int status, Date created, List<Role> role) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.phone = phone;
//        this.status = status;
//        this.created = created;
//        this.role = role;
//    }
//    public User() {
//
//    }
}
