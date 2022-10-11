package com.service.mis.data.register;

import com.service.mis.entity.register.RegisterEnumKey;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnumKeyRepository extends PagingAndSortingRepository<RegisterEnumKey,Integer> {
//    RegisterEnumKey findIdByName(String name);
}
