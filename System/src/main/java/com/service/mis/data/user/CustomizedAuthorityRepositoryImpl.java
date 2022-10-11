package com.service.mis.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomizedAuthorityRepositoryImpl implements CustomizedAuthorityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public long deleteRoleAndAuthority(Long role_id, Long authority_id) {
        long update = jdbcTemplate.update("delete from role_authority where role_id=" + role_id + " and authority_id=" + authority_id + "");
        return update;
    }
}
