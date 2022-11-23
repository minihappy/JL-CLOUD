package com.service.source.entity.handbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hjl
 * @version 1.0
 * @description 手册模板
 * @date 2022/11/17 9:58
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Handbook_Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private final String template_name;
    private final String creator;
    private final int is_default;
    @CreatedDate
    private Date created_time;
    @UpdateTimestamp
    private Date update_time;
    @Column(length = 500)
    private final String modifier;
}
