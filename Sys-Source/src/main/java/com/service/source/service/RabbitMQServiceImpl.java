package com.service.source.service;

import com.service.common.config.RabbitMQConfig;
import com.service.source.service.impl.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author hjl
 * @version 1.0
 * @description 消息发送
 * @date 2022/11/20 16:43
 */
@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    //日期格式化
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RabbitTemplate rabbitTemplate;

    private Map<String, Object> msgToMap(String msg) {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", msg);
        return map;
    }

    @Override
    public String sendMsg(String msg) throws Exception {
        try {
            Map<String, Object> map = msgToMap(msg);
            rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_DIRECT_EXCHANGE, RabbitMQConfig.RABBITMQ_DIRECT_ROUTING, map);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public String sendMsgByFanoutExchange(String msg) throws Exception {
        return null;
    }

    /**
     *
     * @param msg:要发送的消息队列
     * @param routingKey：要匹配的路由
     * @return
     * @throws Exception
     */
    @Override
    public String sendMsgByTopicExchange(String msg, String routingKey) throws Exception {
        try {
            Map<String, Object> map = msgToMap(msg);
            rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_DIRECT_EXCHANGE, routingKey, map);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public String sendMsgByHeadersExchange(String msg, Map<String, Object> map) throws Exception {
        return null;
    }
}
