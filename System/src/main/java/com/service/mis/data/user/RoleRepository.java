package com.service.mis.data.user;

import com.service.mis.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role,Long>,CustomizedRoleRepository {
    Page<Role> findRoleByNameLike(String name, Pageable pageable);

}
