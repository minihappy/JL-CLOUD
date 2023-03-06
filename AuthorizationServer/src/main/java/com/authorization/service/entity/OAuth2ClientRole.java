package com.authorization.service.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hjl
 * @version 1.0
 * @description 客户端的实体
 * @date 2022/10/20 21:34
 */
@Data
@Entity
@Table(name = "`oauth2_client_role`")
public class OAuth2ClientRole {
    @Id
    private Long id;
    private String clientRegistrationId;
    private String roleName;

    @ManyToOne
    @JoinTable(
            name = "oauth2_client_role_mapping",
            joinColumns = {
                    @JoinColumn(name = "oauth_client_role_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private Role role;
}
