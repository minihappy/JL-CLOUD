package com.service.source.data.user;

import com.service.source.entity.Authority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long>, CustomizedAuthorityRepository {
    Authority findAuthorityById(Long id);

    //
//@Query("select count(e) from #{#entityName} e where e.id=?1 ")
    long countAuthorityByParentId(Long id);
//    @Query(value="delete role_authortity form role_id=?1 and authotity_id=?1")
//    int deleteRoleAndAuthority(Long role_id,Long authority_id);
}
