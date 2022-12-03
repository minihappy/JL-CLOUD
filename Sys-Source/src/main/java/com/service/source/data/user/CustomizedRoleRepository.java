package com.service.source.data.user;

import com.service.source.dto.RoleAuthorityDto;
import com.service.source.entity.Authority;

import java.util.List;

public interface CustomizedRoleRepository {
    List<Authority> findAuthorityById(Long id);

    List<RoleAuthorityDto> findRoleById(Long id);

    int deleteRoleAuthRelation(Long role_id);

    int addRoleAuthRelation(Long role_id, Long authority_id);

    void deleteRoleAllRelation(Long[] role_id);
}
