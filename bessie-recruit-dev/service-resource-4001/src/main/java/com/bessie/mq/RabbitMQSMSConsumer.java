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
    @RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE}) //绑定的就是 sms_queue 这个队列
    public void watchQueue(String payload, Message message) throws Exception {

        log.info("payload = " + payload);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = " + routingKey);

        String msg = payload;
        log.info("msg = " + msg);

        if (routingKey.equalsIgnoreCase(RabbitMQSMSConfig.ROUTING_KEY_SMS_SEND_LOGIN)) {
            // 此处为短信发送的消息消费处理
            SMSContentQO contentQO = GsonUtils.stringToBean(msg, SMSContentQO.class);
            smsUtils.sendSMS(contentQO.getMobile(), contentQO.getContent());
        }
    }
}
