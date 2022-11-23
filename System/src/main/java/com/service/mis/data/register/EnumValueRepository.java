package com.service.mis.data.register;

import com.service.mis.entity.register.RegisterEnumValue;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnumValueRepository extends PagingAndSortingRepository<RegisterEnumValue, Integer> {
//    RegisterEnumValue findIdByName(String name);
}
