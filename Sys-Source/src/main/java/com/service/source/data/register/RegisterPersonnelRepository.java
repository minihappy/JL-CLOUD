package com.service.source.data.register;

import com.service.source.entity.register.RegisterPersonnel;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RegisterPersonnelRepository extends PagingAndSortingRepository<RegisterPersonnel, Long> {
    List<RegisterPersonnel> findRegisterPersonnelByNameLike(String name, PageRequest pageable);

    @Transactional//注解用于提交事务，若没有带上这句，会报事务异常提示。
    @Modifying(clearAutomatically = true)//自动清除实体里保存的数据。
    @Query(value = "update RegisterPersonnel set status =1 where id in(:id)")//这里使用了不是原生的sql语句，所以不要加nativeQuery = true。
    void Pass(@Param("id") List<Long> id);

    @Transactional
    @Modifying(clearAutomatically = true)//自动清除实体里保存的数据。
    @Query(value = "update RegisterPersonnel set status =2 where id in(:id)")//这里使用了不是原生的sql语句，所以不要加nativeQuery = true。
    void unPass(@Param("id") List<Long> id);

}
