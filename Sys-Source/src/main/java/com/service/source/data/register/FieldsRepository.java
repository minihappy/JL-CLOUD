package com.service.source.data.register;

import com.service.source.entity.register.RegisterFields;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FieldsRepository extends PagingAndSortingRepository<RegisterFields, Integer> {
}
