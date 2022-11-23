package com.service.source.data.user;

public interface CustomizedAuthorityRepository {
    long deleteRoleAndAuthority(Long role_id, Long authority_id);
}
