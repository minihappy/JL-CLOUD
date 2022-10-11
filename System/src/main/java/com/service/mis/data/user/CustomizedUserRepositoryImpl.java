package com.service.mis.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int deleteUserRoleRelationByUserId(Long user_id) {
        int update = jdbcTemplate.update("delete from user_role  where user_id=" + user_id + "");
        return update;
    }

    @Override
    public int saveUserRoleRelation(Long user_id, Long[] role_ids) {
        int count = 0;
        for (Long role_id : role_ids) {
            int update = jdbcTemplate.update("insert into user_role (user_id,role_id) values (" + user_id + "," + role_id + ")");
            count = update > 0 ? count+1 : count;
        }
        return count;
    }

    @Override
    public int changePassWord(Long user_id, String newPass) {
        String sql = "update user set password='" + newPass + "' where id=" + user_id + "";
        int update = jdbcTemplate.update(sql);
        return update;
    }

    @Override
    public int deleteUserByIds(Long[] ids) {
        String sql;
        int count = 0;
        for (int i = 0; i < ids.length; i++) {
            count++;
            sql = "delete from user where id =" + ids[i] + "";
            jdbcTemplate.update(sql);
        }
        return count;
    }
}
