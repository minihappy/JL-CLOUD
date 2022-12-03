package com.service.register.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author hjl
 * @version 1.0
 * @description 默认redis配置类
 * 为什么要配置该类：
 * 因为redis存数据时，比如需要存放hash等数据，需要传map的参数，此时要根据该配置的序列化器
 * 将map的key，value依次读取，依次存入redis，故需要配置相应的序列化器
 * @date 2022/9/20 16:13
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        // key采用String的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // value序列化方式采用jackson
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // hash的value序列化方式采用jackson
        //jackson2对比genericJackson2区别
        //generic:会存储对象的信息
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        return template;
    }
}
