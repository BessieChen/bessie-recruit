package com.bessie.mq;

import com.bessie.api.mq.RabbitMQSMSConfig_Dead;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 死信队列监听消费者
 */
@Slf4j
@Component
public class RabbitMQSMSConsumer_Dead {

    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {RabbitMQSMSConfig_Dead.SMS_QUEUE_DEAD})
    public void watchQueue(Message message, Channel channel) throws Exception {

        log.warn("++++++++++++++++++++++++++++++");

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("routingKey = " + routingKey);

        String msg = new String(message.getBody());
        log.warn("msg = " + msg);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        true);

        log.warn("++++++++++++++++++++++++++++++");
    }
}
