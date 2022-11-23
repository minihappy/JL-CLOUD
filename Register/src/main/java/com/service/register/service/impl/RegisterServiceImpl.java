package com.service.register.service.impl;

import com.service.register.entity.Register;
import com.service.register.mapper.RegisterMapper;
import com.service.register.service.IRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjl
 * @since 2022-11-16
 */
@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Register> implements IRegisterService {

}
