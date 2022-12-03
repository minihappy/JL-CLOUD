package com.service.source.config.rabbit.consumer;

import com.alibaba.fastjson.JSONObject;
import com.service.common.config.RabbitMQConfig;
import com.service.source.utils.RedisCache;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hjl
 * @version 1.0
 * @description 消费rabbit
 * @date 2022/11/20 17:16
 */
@Component
@RabbitListener(queues = {RabbitMQConfig.System.RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_QUEUE_A, RabbitMQConfig.System.RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_QUEUE_B})
public class RabbitMQConsumer {
    @Resource
    private RedisCache redisCache;

    @RabbitHandler
    public void process(Map message) {
        Object msg = message.get("msg");
        if (msg instanceof JSONObject) {

        } else {
            //补：日志记录操作
        }
        System.out.println("消费mq" + message.toString());
    }
}
