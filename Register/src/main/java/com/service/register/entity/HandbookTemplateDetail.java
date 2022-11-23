package com.service.register.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjl
 * @since 2022-11-17
 */
@TableName("handbook_template_detail")
@ApiModel(value = "HandbookTemplateDetail对象", description = "")
public class HandbookTemplateDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Integer able;

    private Integer templateId;

    private Integer reference;

    private String referenceId;

    private Integer sort;

    private LocalDateTime updateTime;

    private String modifier;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAble() {
        return able;
    }

    public void setAble(Integer able) {
        this.able = able;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "HandbookTemplateDetail{" +
        "id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", able=" + able +
        ", templateId=" + templateId +
        ", reference=" + reference +
        ", referenceId=" + referenceId +
        ", sort=" + sort +
        ", updateTime=" + updateTime +
        ", modifier=" + modifier +
        "}";
    }
}
