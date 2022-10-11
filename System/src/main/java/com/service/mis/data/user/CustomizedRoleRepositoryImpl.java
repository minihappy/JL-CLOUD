package com.service.mis.data.user;

import com.alibaba.fastjson.JSONArray;
import com.service.mis.dto.RoleAuthorityDto;
import com.service.mis.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CustomizedRoleRepositoryImpl implements CustomizedRoleRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int[] batchUpdate(String... sqlArr) {
        int[] ints = jdbcTemplate.batchUpdate(sqlArr);
        return ints;
    }

    @Override
    public List<Authority> findAuthorityById(Long id) {
        return null;
    }

    @Override
    public List<RoleAuthorityDto> findRoleById(Long id) {

        List<RoleAuthorityDto> maps = jdbcTemplate.query("select id,component,icon,name,order_num,parent_id,path,perms,`status`,type,authority_id from (select authority_id from role_authority where role_id=?)as ra right JOIN authority as a on ra.authority_id=a.id", new Object[]{id}, new BeanPropertyRowMapper<RoleAuthorityDto>(RoleAuthorityDto.class));
        return maps;
    }

    @Override
    public int deleteRoleAuthRelation(Long role_id) {
        String sql = "delete from role_authority where role_id =" + role_id + "";
        int count = jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public int addRoleAuthRelation(Long role_id, Long authority_id) {
        String sql = "INSERT INTO role_authority(role_id,authority_id) values (" + role_id + ", " + authority_id + ")";
        int count = jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public int deleteRoleAllRelation(Long[] role_ids) {
        String roleUser, roleAuthority;
        String[] sqlArray = new String[role_ids.length * 2];
        for (int i = 0; i < role_ids.length; i++) {
            roleUser = "delete from user_role where role_id = " + role_ids[i] + "";
            roleAuthority = "delete from role_authority where role_id = " + role_ids[i] + "";
            sqlArray[i] = roleUser;
            sqlArray[i + 1] = roleAuthority;
        }
        int[] ints = batchUpdate(sqlArray);
        return ints.length/2;
    }
}
