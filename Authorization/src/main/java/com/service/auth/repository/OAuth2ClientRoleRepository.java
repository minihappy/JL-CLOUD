package com.service.auth.repository;

import com.service.auth.entity.OAuth2ClientRole;
import com.service.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hjl
 * @version 1.0
 * @description 客户端仓库
 * @date 2022/10/20 21:32
 */
public interface OAuth2ClientRoleRepository extends JpaRepository<OAuth2ClientRole, String> {
    OAuth2ClientRole findByClientRegistrationIdAndRoleName(String clientId, String roleName);
}
