package com.service.source.entity.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author hjl
 * @version 1.0
 * @description 手册
 * @date 2022/11/17 10:07
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Handbook_Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BIGINT")
    private Long register_id;
    @Column(columnDefinition = "BIGINT")
    private Long user_id;
}
