package com.service.source.data.register;

import com.service.source.entity.register.RegisterEnumValue;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnumValueRepository extends PagingAndSortingRepository<RegisterEnumValue, Integer> {
//    RegisterEnumValue findIdByName(String name);
}
