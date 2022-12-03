package com.service.common.config;

/**
 * @author hjl
 * @version 1.0
 * @description 通用rabbitmq配置
 * @date 2022/11/20 15:57
 */
public class RabbitMQConfig {

    public static class System {
        public static final String RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_EXCHANGE = "rabbitmq.system.change.role.topic.exchange";
        public static final String RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_QUEUE_A = "rabbitmq.system.change.role.topic.queue.a";
        public static final String RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_QUEUE_B = "rabbitmq.system.change.role.topic.queue.b";
    }

    public static class Register {
        public static final String RABBITMQ_REGISTER_SENT_MAIL_TOPIC_EXCHANGE = "rabbitmq.register.sent.mail.topic.exchange";
        public static final String RABBITMQ_REGISTER_SENT_MAIL_TOPIC_QUEUE = "rabbitmq.register.sent.mail.topic.queue";
    }
}
