package com.bessie.mq;

import com.bessie.api.mq.InitResumeMQConfig;
import com.bessie.service.ResumeService;
import com.bessie.utils.SMSUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化简历的消费者
 */
@Slf4j
@Component
public class InitResumeMQConsumer {

    @Autowired
    private ResumeService resumeService;

    /**
     * 监听队列，并且处理消息
     * @param payload
     * @param message
     */
    @RabbitListener(queues = {InitResumeMQConfig.INIT_RESUME_QUEUE})
    public void watchQueue(String payload, Message message) throws Exception {

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey = " + routingKey);

        String userId = payload;
        log.info("userId = " + userId);

        if (routingKey.equalsIgnoreCase(InitResumeMQConfig.ROUTING_KEY_INIT_RESUME)) {
            resumeService.initResume(userId);
        }
    }

}
