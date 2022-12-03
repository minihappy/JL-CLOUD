package com.service.register.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.service.common.config.RabbitMQConfig;
import com.service.register.entity.RegisterPersonnel;
import com.service.register.mapper.RegisterPersonnelMapper;
import com.service.register.service.IRegisterPersonnelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjl
 * @since 2022-11-17
 */
@Service
public class RegisterPersonnelServiceImpl extends ServiceImpl<RegisterPersonnelMapper, RegisterPersonnel> implements IRegisterPersonnelService {
    @Resource
    RegisterPersonnelMapper registerPersonnelMapper;

    @Resource
    RabbitMQServiceImpl rabbitMQServiceImpl;

    @SneakyThrows
    @Override
    public boolean sentProcessToEmail(String email) {
        List<RegisterPersonnel> regPersonnel = registerPersonnelMapper.selectProcessToEmail(email);//ArrayList
        if (regPersonnel == null) {
            return false;
        } else {
            String s = JSONObject.toJSONString(regPersonnel);
            rabbitMQServiceImpl.sendMsgByTopicExchange(s, "send.mail");
            return true;
        }

    }
}
