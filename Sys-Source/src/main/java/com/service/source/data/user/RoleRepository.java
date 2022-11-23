package com.service.source.data.user;

import com.service.source.entity.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long>, CustomizedRoleRepository {
    Page<Role> findRoleByNameLike(String name, Pageable pageable);

    /**
     * 涉及到数据修改操作，可以使用 @Modifying 注解， @Query 与 @Modifying 这两个 annotation一起声明，可定义个性化更新操作
     * @param role_id
     * @param authority_id
     * @return
     */
//    @Modifying
////    @Transactional
//    @Query(value = "insert into role_authority(role_id,authority_id) values (:role_id,:authority_id )", nativeQuery = true)
//    void addRoleAuthRelation(@Param("role_id") Long role_id, @Param("authority_id") Long authority_id);
}
