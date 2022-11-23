package com.service.source.data.user;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomizedUserRepository {
    int deleteUserRoleRelationByUserId(Long user_id);

    int saveUserRoleRelation(Long user_id, Long[] role_ids);

    int changePassWord(Long user_id, String newPass);

//    int deleteUserByIds(Long[] ids);
}
