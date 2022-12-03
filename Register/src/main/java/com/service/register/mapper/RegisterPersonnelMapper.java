package com.service.register.mapper;

import com.service.register.entity.RegisterPersonnel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hjl
 * @since 2022-11-17
 */
@Mapper
public interface RegisterPersonnelMapper extends BaseMapper<RegisterPersonnel> {

    List<RegisterPersonnel> selectProcessToEmail(@Param("email") String email);
}
