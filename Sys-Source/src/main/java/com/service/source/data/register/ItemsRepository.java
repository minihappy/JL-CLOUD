package com.service.source.data.register;

import com.service.source.entity.register.RegisterItems;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemsRepository extends PagingAndSortingRepository<RegisterItems, Integer> {
}
