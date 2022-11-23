package com.service.mis.data.user;

import com.service.mis.entity.Role;
import com.service.mis.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, CustomizedUserRepository {
    User findByUsername(String username);

    User findUserById(Long id);

    void deleteUserById(Long id);

    Page<User> findUserByUsernameLike(String userName, Pageable pageable);
}
