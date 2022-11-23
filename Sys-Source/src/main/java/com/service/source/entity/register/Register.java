package com.service.source.entity.register;

import com.service.source.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * CREATE TABLE register (
 * id BIGINT PRIMARY KEY AUTO_INCREMENT,
 * name varchar(50) NOT NULL,
 * administrator_id BIGINT,
 * contents varchar(100),
 * created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
 * update_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
 * FOREIGN KEY(administrator_id) REFERENCES user(id)
 * );
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private final List<User> user;
    private final String name;
    private final String contents;
    @CreatedDate
    private Date created_at;
    @UpdateTimestamp
    private Date update_at;
//    @ManyToMany(targetEntity = User.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "class_id")
//    private List<User> users;
//    private Date starTime;
//    private Date endTime;
//    @Column(name = "submitTime", columnDefinition = "DATETIME NOT NULL default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private Date submitTime;
}
