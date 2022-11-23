package com.service.auth.repository;


import com.service.auth.entity.Role;
import com.service.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
