package com.service.source.entity.register;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Register_personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String name;
    private final String email;
    private final String id_card;
    private final String phone;
    private final String photo;
    @ManyToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.ForeignKey(name = "none")
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))//分布式不推荐外键级连操作，保证数据一致性请在业务层实现
    private List<Register> registers;
    private int status;
    @CreatedDate
    private Date created_at;
    @UpdateTimestamp
    private Date update_at;
    private final String check_man;
    private Long handbook_use;
    @Column(length = 500)
    private String handbook_modifier;
    Date handbook_update_time;
    Date handbook_template_id;
}
