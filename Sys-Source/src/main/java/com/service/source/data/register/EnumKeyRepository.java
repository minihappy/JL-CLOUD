package com.service.source.data.register;

        import com.service.source.entity.register.RegisterEnumKey;

        import org.springframework.data.repository.PagingAndSortingRepository;

public interface EnumKeyRepository extends PagingAndSortingRepository<RegisterEnumKey, Integer> {
//    RegisterEnumKey findIdByName(String name);
}
