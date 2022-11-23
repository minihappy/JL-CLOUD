package com.service.source.service.impl;

import java.util.Map;

/**
 * @author hjl
 * @description rabbit
 * @date 2022/11/20 16:47
 */
public interface RabbitMQService {

    String sendMsg(String msg) throws Exception;

    String sendMsgByFanoutExchange(String msg) throws Exception;

    String sendMsgByTopicExchange(String msg, String routingKey) throws Exception;

    String sendMsgByHeadersExchange(String msg, Map<String, Object> map) throws Exception;
}
