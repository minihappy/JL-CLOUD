package com.service.common.config;

/**
 * @author hjl
 * @version 1.0
 * @description 通用rabbitmq配置
 * @date 2022/11/20 15:57
 */
public class RabbitMQConfig {

    /**
     * RabbitMQ的队列主题名称
     */
    public static final String RABBITMQ_TOPIC = "rabbitmq.topic";

    /**
     * RabbitMQ的DIRECT交换机名称
     */
    public static final String RABBITMQ_DIRECT_EXCHANGE = "rabbitmq.direct.exchange";

    /**
     * RabbitMQ的DIRECT交换机和队列绑定的匹配键 DirectRouting
     */
    public static final String RABBITMQ_DIRECT_ROUTING = "rabbitmq.direct.routing";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 A 的名称
     */
    public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_A = "fanout.A";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的队列 B 的名称
     */
    public static final String FANOUT_EXCHANGE_QUEUE_TOPIC_B = "fanout.B";

    /**
     * RabbitMQ的FANOUT_EXCHANG交换机类型的名称
     */
    public static final String FANOUT_EXCHANGE_ROLE_AUTHORITY_CHANGE = "fanout.exchange.role.authority.change";

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机名称
     */
    public static final String TOPIC_EXCHANGE_ROLE_AUTHORITY_CHANGE = "topic.exchange.role.authority.change";

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机的队列A的名称
     */
    public static final String TOPIC_ROLE_AUTHORITY_CHANGE_QUEUE_A = "topic.role.authority.change.queue.a";

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机的队列B的名称
     */
    public static final String TOPIC_ROLE_AUTHORITY_CHANGE_QUEUE_B = "topic.role.authority.change.queue.b";

    /**
     * RabbitMQ的TOPIC_EXCHANGE交换机的队列C的名称
     */
    public static final String TOPIC_ROLE_AUTHORITY_CHANGE_QUEUE_C = "topic.role.authority.change.queue.c";

    /**
     * HEADERS_EXCHANGE交换机名称
     */
    public static final String HEADERS_EXCHANGE_NAME = "headers.exchange.name";

    /**
     * RabbitMQ的HEADERS_EXCHANGE交换机的队列A的名称
     */
    public static final String HEADERS_EXCHANGE_QUEUE_A = "headers.queue.a";

    /**
     * RabbitMQ的HEADERS_EXCHANGE交换机的队列B的名称
     */
    public static final String HEADERS_EXCHANGE_QUEUE_B = "headers.queue.b";

}
