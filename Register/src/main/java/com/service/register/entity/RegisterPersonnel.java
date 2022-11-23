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
@TableName("register_personnel")
@ApiModel(value = "RegisterPersonnel对象", description = "")
public class RegisterPersonnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String idCard;

    private String phone;

    private String photo;

    private Long registerId;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private String checkMan;

    private Integer handbookUse;

    private String handbookModifier;

    private LocalDateTime handbookUpdateTime;

    private Long handbookTemplateId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public Integer getHandbookUse() {
        return handbookUse;
    }

    public void setHandbookUse(Integer handbookUse) {
        this.handbookUse = handbookUse;
    }

    public String getHandbookModifier() {
        return handbookModifier;
    }

    public void setHandbookModifier(String handbookModifier) {
        this.handbookModifier = handbookModifier;
    }

    public LocalDateTime getHandbookUpdateTime() {
        return handbookUpdateTime;
    }

    public void setHandbookUpdateTime(LocalDateTime handbookUpdateTime) {
        this.handbookUpdateTime = handbookUpdateTime;
    }

    public Long getHandbookTemplateId() {
        return handbookTemplateId;
    }

    public void setHandbookTemplateId(Long handbookTemplateId) {
        this.handbookTemplateId = handbookTemplateId;
    }

    @Override
    public String toString() {
        return "RegisterPersonnel{" +
        "id=" + id +
        ", name=" + name +
        ", email=" + email +
        ", idCard=" + idCard +
        ", phone=" + phone +
        ", photo=" + photo +
        ", registerId=" + registerId +
        ", status=" + status +
        ", createdAt=" + createdAt +
        ", updateAt=" + updateAt +
        ", checkMan=" + checkMan +
        ", handbookUse=" + handbookUse +
        ", handbookModifier=" + handbookModifier +
        ", handbookUpdateTime=" + handbookUpdateTime +
        ", handbookTemplateId=" + handbookTemplateId +
        "}";
    }
}
