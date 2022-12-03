package com.service.source.data.register;

        import com.service.source.dto.RegisterDao;
        import com.service.source.entity.register.Register;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.PagingAndSortingRepository;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;
        import java.util.Map;

/**
 * @author hjl
 * @description
 * @date 2022/11/24 13:36
 */
public interface RegisterRepository extends PagingAndSortingRepository<Register, Long> {
    List<Register> findRegisterByNameLike(String name, PageRequest pageable);

    @Transactional
    @Query(value = "select r.id,r.name from register r where r.start_time_at <=now() and r.end_time_at>=now()", nativeQuery = true)
    List<RegisterDao> findDoingRegister();

}
