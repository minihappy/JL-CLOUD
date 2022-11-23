package com.service.register.config.rabbit.consumer;

import com.service.common.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hjl
 * @description
 * @date 2022/11/23 10:36
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConfig.TOPIC_ROLE_AUTHORITY_CHANGE_QUEUE_B))
public class FanoutExchangeConsumerB {

    @RabbitHandler
    public void process(Map<String, Object> map) {
        System.out.println("队列B收到消息：" + map.toString());
    }

}
