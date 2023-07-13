package com.bessie.mq;

import com.bessie.pojo.MqLocalMsgRecord;
import com.bessie.service.MqLocalMsgRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bessie-recruit-dev
 * @description: MQ消息处理助手
 * @author: Bessie
 * @create: 2023-07-13 15:33
 **/

@Component
public class InitResumeMQProducerHandler {

    // 存储本地消息id列表
    private ThreadLocal<List<String>> msgIdsThreadLocal = new ThreadLocal<>();

    @Autowired
    private MqLocalMsgRecordService recordService;

    /**
     * 保存消息到本地数据库
     * @param targetExchange
     * @param routingKey
     * @param msgContent
     */
    public void saveLocalMsg(String targetExchange, String routingKey, String msgContent) {

        MqLocalMsgRecord record = new MqLocalMsgRecord();
        record.setTargetExchange(targetExchange);
        record.setRoutingKey(routingKey);
        record.setMsgContent(msgContent);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());

        recordService.save(record);

        // 从threadLocal中获得本地消息id，如果为空则初始化
        List<String> msgIds = msgIdsThreadLocal.get();
        if (CollectionUtils.isEmpty(msgIds)) {
            msgIds = new ArrayList<>();
        }
        // 每次消息存储在本地表中之后，都需要存储在当前线程中，事务提交以后可以对其进行检查
        msgIds.add(record.getId());
        msgIdsThreadLocal.set(msgIds);
    }

}
