package com.service.source.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
//@NoArgsConstructor
//@AllArgsConstructor
public class User implements UserDetails {

//    private static final long serialVersionUID = -1681695020636685631L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int status;
    /*    @CreatedDate
        private Date created;*/
//    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<Role> role;
    private String createBy;
    private String changeBy;
    @CreatedDate
    private Date createTime;
    @UpdateTimestamp
    private Date changeTime;
    private boolean enabled = false;

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

    /**
     * 返回权限的字段
     * 问题1：会报Cannot construct instance of `org.springframework.security.core.GrantedAuthority`
     * 根本问题，com.fasterxml.jackson在反序列化时，会读取类的反射查找具体有没有这个字段，没有会报异常
     * 以下是我的堆栈分析
     * at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:67) ~[jackson-databind-2.13.3.jar:2.13.3]//抛出异常
     * at com.fasterxml.jackson.databind.DeserializationContext.reportBadDefinition(DeserializationContext.java:1904) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:400) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.DeserializationContext.handleMissingInstantiator(DeserializationContext.java:1349) ~[jackson-databind-2.13.3.jar:2.13.3]//执行缺少实例的方法
     * at com.fasterxml.jackson.databind.deser.AbstractDeserializer.deserialize(AbstractDeserializer.java:274) ~[jackson-databind-2.13.3.jar:2.13.3]//开始反序列化
     * at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer._deserializeFromArray(CollectionDeserializer.java:355) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:272) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.deserialize(CollectionDeserializer.java:28) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.deser.impl.SetterlessProperty.deserializeAndSet(SetterlessProperty.java:134) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:391) ~[jackson-databind-2.13.3.jar:2.13.3]//根据字段，查找实例，如果缺少该字段就报错，代码如下：SettableBeanProperty prop = this._beanProperties.find(propName);
     * at com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:184) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:323) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:4674) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3682) ~[jackson-databind-2.13.3.jar:2.13.3]
     * at javax.servlet.http.HttpServlet.service(HttpServlet.java:681) ~[tomcat-embed-core-9.0.63.jar:4.0.FR]//通过http请求，判断post还是get等请求，判断进行后续程序
     * at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.63.jar:4.0.FR]
     *
     * @return
     */
    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)//自定义的序列化
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
