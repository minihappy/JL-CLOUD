package com.service.source.data.user;


import com.service.source.entity.User;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, CustomizedUserRepository {
    User findByUsername(String username);

    User findUserById(Long id);

    void deleteUserById(Long id);

    Page<User> findUserByUsernameLike(String userName, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "select user_id from user_role  where  role_id in (:id) ", nativeQuery = true)
    List<Long> findUserIdByRoleId(@Param("id") Long roleId);

    @Modifying
    @Transactional
    @Query("delete  from User u  where  u.id in (:id) ")
    int deleteUserByIds(@Param("id") List<Long> id);
}
