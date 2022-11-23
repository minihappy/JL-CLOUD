package com.service.source.entity.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author hjl
 * @version 1.0
 * @description 手册日志
 * @date 2022/11/17 10:53
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Handbook_Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private final String template_name;
    @Column(length = 500)
    private final String operation_type;
    @Column(length = 500)
    private final String operation_content;
    @Column(length = 500)
    private final String operation_time;
    @Column(length = 500)
    private final String operation_user;
    @Column(length = 500)
    private final String operation_model;
}
