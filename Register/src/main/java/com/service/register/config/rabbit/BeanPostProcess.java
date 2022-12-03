package com.service.register.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author hjl
 * @description
 * @date 2022/11/30 0:41
 */
@Configuration
public class BeanPostProcess implements BeanPostProcessor {
    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    @Qualifier("rabbitmqTopicExchange")
    private TopicExchange rabbitmqTopicExchange;
    @Resource
    @Qualifier("rabbitmqTopicQueue")
    private Queue rabbitmqTopicQueue;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {//bean初始化之后进行操作
        rabbitAdmin.declareExchange(rabbitmqTopicExchange);//创建交换机
        rabbitAdmin.declareQueue(rabbitmqTopicQueue);//创建消息队列
        return null;
    }
}
