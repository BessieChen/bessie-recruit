package com.bessie.api.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: bessie-recruit-dev
 * @description: RabbitMQ 的配置类
 * @author: Bessie
 * @create: 2023-07-07 08:13
 **/
@Configuration
public class RabbitMQSMSConfig {

    // 定义交换机的名称
    public static final String SMS_EXCHANGE = "sms_exchange";

    // 定义队列的名称
    public static final String SMS_QUEUE = "sms_queue";

    // 统一定义路由key
    public static final String ROUTING_KEY_SMS_SEND_LOGIN = "bessie.sms.send.login";

    // 创建交换机
    @Bean(SMS_EXCHANGE)
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(SMS_EXCHANGE)
                .durable(true)
                .build();
    }

    // 创建队列
    @Bean(SMS_QUEUE)
    public Queue queue() {
        //return new Queue(SMS_QUEUE);                  //方法1
        return QueueBuilder.durable(SMS_QUEUE)
                .withArgument("x-message-ttl", 20*1000) //20秒
                .withArgument("x-dead-letter-exchange",
                        RabbitMQSMSConfig_Dead.SMS_EXCHANGE_DEAD)
                .withArgument("x-dead-letter-routing-key",
                        RabbitMQSMSConfig_Dead.ROUTING_KEY_SMS_DEAD)
                .withArgument("x-max-length", 6)
                .build(); //方法2
    }

    // 创建绑定关系
    @Bean
    public Binding smsBinding(@Qualifier(SMS_EXCHANGE) Exchange exchange,
                              @Qualifier(SMS_QUEUE) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("bessie.sms.#")
                .noargs();
    }
}
