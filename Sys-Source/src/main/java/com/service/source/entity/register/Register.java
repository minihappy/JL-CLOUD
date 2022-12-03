package com.service.source.entity.register;

import com.service.source.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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
//@SqlResultSetMapping(
//        name = "RegisterResult",
//        entities = {
//                @EntityResult(
//                        entityClass = com.service.source.entity.register.Register.class,
//                        fields = {@FieldResult(name = "id", column = "id"),
//                                @FieldResult(name = "name", column = "name")}
//                )
//        }
//)
//@org.hibernate.annotations.NamedNativeQuery(
//        name = "selectCurrentRegister",
//        query = "select r.id,r.name from register r where r.start_time_at <=now() and r.end_time_at>=now()",
//        resultSetMapping = "RegisterResult"
//)
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private final List<User> users;
    private final String name;
    private final String contents;
    @ColumnDefault("1")
    private final int status;
    private String updateMan;
    @CreatedDate
    private Date startTimeAt;
    @CreatedDate
    private Date endTimeAt;
    @CreatedDate
    private Date createdAt;
    @UpdateTimestamp
    private Date updateAt;

//    @ManyToMany(targetEntity = User.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "class_id")
//    private List<User> users;
//    private Date starTime;
//    private Date endTime;
//    @Column(name = "submitTime", columnDefinition = "DATETIME NOT NULL default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
//    private Date submitTime;
}
