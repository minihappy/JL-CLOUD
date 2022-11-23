package com.service.register.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("handbook_reference")
@ApiModel(value = "HandbookReference对象", description = "")
public class HandbookReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String templateName;

    private String referenceApi;

    private String referenceId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getReferenceApi() {
        return referenceApi;
    }

    public void setReferenceApi(String referenceApi) {
        this.referenceApi = referenceApi;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String toString() {
        return "HandbookReference{" +
        "id=" + id +
        ", templateName=" + templateName +
        ", referenceApi=" + referenceApi +
        ", referenceId=" + referenceId +
        "}";
    }
}
