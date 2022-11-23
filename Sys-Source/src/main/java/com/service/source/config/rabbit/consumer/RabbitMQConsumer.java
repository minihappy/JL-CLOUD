package com.service.source.config.rabbit.consumer;

import com.service.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hjl
 * @version 1.0
 * @description 消费rabbit
 * @date 2022/11/20 17:16
 */
@Component
@RabbitListener(queues = {RabbitMQConfig.RABBITMQ_TOPIC})
public class RabbitMQConsumer {
    @RabbitHandler
    public void process(Map message) {
        System.out.println("消费mq" + message.toString());
    }
}
