package com.service.register.config.rabbit;

import com.service.common.config.RabbitMQConfig;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author hjl
 * @version 1.0
 * @description rabbit配置
 * @date 2022/11/20 15:37
 */
@Component
@Configuration
public class ExchangeRabbitConfig{
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public TopicExchange rabbitmqTopicExchange() {
        //Topic交换机
        return new TopicExchange(RabbitMQConfig.Register.RABBITMQ_REGISTER_SENT_MAIL_TOPIC_EXCHANGE);
    }

    @Bean
    public Queue rabbitmqTopicQueue() {
        /**
         * 1、name:    队列名称
         * 2、durable: 是否持久化
         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
         * */
        return new Queue(RabbitMQConfig.Register.RABBITMQ_REGISTER_SENT_MAIL_TOPIC_QUEUE, true, false, false);
    }

//    @Bean
//    public Queue rabbitmqTopicQueue_B() {
//        /**
//         * 1、name:    队列名称
//         * 2、durable: 是否持久化
//         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
//         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
//         * */
//        return new Queue(RabbitMQConfig.System.RABBITMQ_SYSTEM_CHANGE_ROLE_TOPIC_QUEUE_B, true, false, false);
//    }

    @Bean
    public Binding bindTopic() {//通配符
        //链式写法，绑定交换机和队列，并设置匹配键
        return BindingBuilder
                //绑定队列
                .bind(rabbitmqTopicQueue())
                //到交换机
                .to(rabbitmqTopicExchange())
                //并设置匹配键
                .with("send.mail");
    }

//    @Bean
//    public Binding bindTopic_B() {//通配符
//        //链式写法，绑定交换机和队列，并设置匹配键
//        return BindingBuilder
//                //绑定队列
//                .bind(rabbitmqTopicQueue_B())
//                //到交换机
//                .to(rabbitmqTopicExchange())
//                //并设置匹配键
//                .with("change.role.*");
//    }

//    @Bean
//    public Queue rabbitmqDirectQueue() {
//        /**
//         * 1、name:    队列名称
//         * 2、durable: 是否持久化
//         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
//         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
//         * */
//        return new Queue(RabbitMQConfig.RABBITMQ_DIRECT_EXCHANGE, true, false, false);
//    }
//
//    @Bean
//    public DirectExchange rabbitmqDirectExchange() {
//        //Direct交换机
//        return new DirectExchange(RabbitMQConfig.RABBITMQ_DIRECT_EXCHANGE, true, false);
//    }
//
//    @Bean
//    public FanoutExchange rabbitmqFanoutExchange() {
//        //fanout交换机
//        return new FanoutExchange(RabbitMQConfig.FANOUT_EXCHANGE_ROLE_AUTHORITY_CHANGE);
//    }
//
//    @Bean
//    public Binding bindDirect() {//点对点
//        //链式写法，绑定交换机和队列，并设置匹配键
//        return BindingBuilder
//                //绑定队列
//                .bind(rabbitmqDirectQueue())
//                //到交换机
//                .to(rabbitmqDirectExchange())
//                //并设置匹配键
//                .with(RabbitMQConfig.RABBITMQ_DIRECT_ROUTING);
//    }

    //实例化bean后，也就是Bean的后置处理器

}
