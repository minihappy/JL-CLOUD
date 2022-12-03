package com.service.register.config.rabbit.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.common.config.RabbitMQConfig;

import com.service.register.service.IMailService;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hjl
 * @description
 * @date 2022/11/29 20:52
 */
@Component
@RabbitListener(queues = {RabbitMQConfig.Register.RABBITMQ_REGISTER_SENT_MAIL_TOPIC_QUEUE})
public class RabbitMQEmailConsumer {
    @Resource
    IMailService mail;

    @RabbitHandler
    public void process(Map message) {
        Object msg = message.get("msg");

        if (msg != null) {
            JSONArray jsonArray;
            jsonArray = JSONObject.parseArray(msg.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                int status = (int) jsonArray.getJSONObject(i).get("status");
                String registerStatus = status == 0 ? "未审核" : status == 1 ? "已审核" : "审核未通过";
                String name = (String) jsonArray.getJSONObject(i).get("name");
                Object registerId = jsonArray.getJSONObject(i).get("registerId");
                String email = (String) jsonArray.getJSONObject(i).get("email");
                mail.sendSimpleMail(email, "报名进度查询", "[" + name + "]" + ",你当前的报名状态为[" + registerStatus + "]");
            }
        } else {
            //补：日志记录操作
        }
        System.out.println("消费mq" + message.toString());
    }
}
