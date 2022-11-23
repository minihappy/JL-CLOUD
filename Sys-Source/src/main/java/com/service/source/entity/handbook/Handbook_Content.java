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
 * @description 手册内容
 * @date 2022/11/17 10:53
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Handbook_Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private final String title;
    @Column(columnDefinition = "text", length = 2000)
    private final String content;
    @Column(columnDefinition = "BIGINT")
    private final Long register_id;

    private final int template_id;
    @Column(length = 500)
    private final String able;
    @Column(length = 500)
    private final String modifier;
    private final int reference;
    @Column(length = 500)
    private final String reference_id;
    private final int sort;
    private final int upload;
    @Column(columnDefinition = "BIGINT")
    private final Long AffixID;
    private final String creator;
    @CreatedDate
    private Date created_time;
    @UpdateTimestamp
    private Date update_time;
}
