package com.service.mis.data.register;

import com.service.mis.entity.register.RegisterFields;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FieldsRepository extends PagingAndSortingRepository<RegisterFields, Integer> {
}
