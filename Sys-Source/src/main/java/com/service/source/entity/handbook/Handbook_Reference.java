package com.service.source.entity.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author hjl
 * @version 1.0
 * @description 手册
 * @date 2022/11/17 10:02
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Handbook_Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private final String template_name;
    @Column(length = 4000)
    private final String reference_api;
    @Column(length = 500)
    private final String reference_id;
}
