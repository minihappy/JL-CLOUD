package com.service.mis.data.user;

import com.service.mis.dto.RoleAuthorityDto;
import com.service.mis.entity.Authority;

import java.util.List;

public interface CustomizedRoleRepository {
    List<Authority> findAuthorityById(Long id);

    List<RoleAuthorityDto> findRoleById(Long id);

    int deleteRoleAuthRelation(Long role_id);

    int addRoleAuthRelation(Long role_id, Long authority_id);

    int deleteRoleAllRelation(Long[] role_id);
}
