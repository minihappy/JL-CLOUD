package com.authorization.service.repository;

/**
 * @author hjl
 * @version 1.0
 * @description 获取权限相关
 * @date 2022/11/8 12:28
 */


import com.authorization.service.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
