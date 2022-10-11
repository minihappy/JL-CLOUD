package com.service.mis.data.user;

public interface CustomizedAuthorityRepository {
    long deleteRoleAndAuthority(Long role_id, Long authority_id);
}
