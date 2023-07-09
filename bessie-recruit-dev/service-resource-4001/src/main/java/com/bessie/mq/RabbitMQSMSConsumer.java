package com.bessie.mq;

import com.bessie.api.mq.RabbitMQSMSConfig;
import com.bessie.pojo.mq.SMSContentQO;
import com.bessie.utils.GsonUtils;
import com.bessie.utils.SMSUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * @program: bessie-recruit-dev
 * @description: 短信监听消费者
 * @author: Bessie
 * @create: 2023-07-07 08:58
 **/
@Slf4j
@Component
public class RabbitMQSMSConsumer {

    @Autowired
    private SMSUtils smsUtils;

    /**
     * 监听队列，并且处理消息
     * @param payload
     * @param message
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE}) //绑定的就是 sms_queue 这个队列
    public void watchQueue(String payload, Message message) throws Exception {

        log.warn("payload = " + payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("routingKey = " + routingKey);

        String msg = payload;
        log.warn("msg = " + msg);

        if (routingKey.equalsIgnoreCase(RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
            // 此处为短信发送的消息消费处理
            SMSContentQO contentQO = GsonUtils.stringToBean(msg, SMSContentQO.class);
            smsUtils.sendSMS(contentQO.getMobile(), contentQO.getContent());
        }
    }

    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue(Message message, Channel channel) throws Exception {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("routingKey = " + routingKey);

        String msg = new String(message.getBody());
        log.warn("msg = " + msg);

    }

    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue2(Message message, Channel channel) throws Exception {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("routingKey = " + routingKey);

        String msg = new String(message.getBody());
        log.warn("msg = " + msg);

        /**
         * deliveryTag: 消息投递的标签
         * multiple: 批量确认所有消费者获得的消息
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                true);

    }

    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue3(Message message, Channel channel) throws Exception {

        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.warn("routingKey = " + routingKey);

             int a = 1/0;

            String msg = new String(message.getBody());
            log.warn("msg = " + msg);

            /**
             * deliveryTag: 消息投递的标签
             * multiple: 批量确认所有消费者获得的消息
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                    true);
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * requeue: true：重回队列 false：丢弃消息
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    true,
                    true);
//            channel.basicReject();
        }

    }

    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue4(Message message, Channel channel) throws Exception {

        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.warn("routingKey = " + routingKey);

            int a = 1 / 0;

            String msg = new String(message.getBody());
            log.warn("msg = " + msg);

            /**
             * deliveryTag: 消息投递的标签
             * multiple: 批量确认所有消费者获得的消息
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                    true);
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * requeue: true：重回队列 false：丢弃消息
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    true,
                    false);
//            channel.basicReject();
        }

    }


    /**
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    //@RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue5(Message message, Channel channel) throws Exception {

        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.warn("routingKey = " + routingKey);

            String msg = new String(message.getBody());
            log.warn("msg = " + msg);

            /**
             * deliveryTag: 消息投递的标签
             * multiple: 批量确认所有消费者获得的消息
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                    true);
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * requeue: true：重回队列 false：丢弃消息
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    true,
                    false);
//            channel.basicReject();
        }

    }

    @RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue6(Message message, Channel channel) throws Exception {

//        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//        log.warn("routingKey = " + routingKey);
//
//        String msg = new String(message.getBody());
//        log.warn("msg = " + msg);

        channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                false, false);
    }
}
