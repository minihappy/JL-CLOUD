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
@TableName("handbook_log")
@ApiModel(value = "HandbookLog对象", description = "")
public class HandbookLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String tableName;

    private String operationType;

    private String operationContent;

    private String operationTime;

    private String operationUser;

    private String operationModel;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationModel() {
        return operationModel;
    }

    public void setOperationModel(String operationModel) {
        this.operationModel = operationModel;
    }

    @Override
    public String toString() {
        return "HandbookLog{" +
        "id=" + id +
        ", tableName=" + tableName +
        ", operationType=" + operationType +
        ", operationContent=" + operationContent +
        ", operationTime=" + operationTime +
        ", operationUser=" + operationUser +
        ", operationModel=" + operationModel +
        "}";
    }
}
