package com.service.mis.data.register;

import com.service.mis.entity.register.RegisterItems;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemsRepository extends PagingAndSortingRepository<RegisterItems, Integer> {
}
