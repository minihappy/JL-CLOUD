package com.service.mis.data.user;

public interface CustomizedUserRepository {
    int deleteUserRoleRelationByUserId(Long user_id);

    int saveUserRoleRelation(Long user_id, Long[] role_ids);

    int changePassWord(Long user_id, String newPass);

    int deleteUserByIds(Long[] ids);
}
