package com.service.mis.entity.register;

import com.service.mis.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private final String name;
    @ManyToMany(targetEntity = User.class, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private List<User> users;
    private Date starTime;
    private Date endTime;
    @Column(name = "submitTime", columnDefinition = "DATETIME NOT NULL default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date submitTime;
}
