package com.bessie.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bessie
 * @since 2023-07-13
 */
public class MqLocalMsgRecord extends Model<MqLocalMsgRecord> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 目标交换机
     */
    private String targetExchange;

    /**
     * 消息路由
     */
    private String routingKey;

    private String msgContent;

    private LocalDateTime createTime;

    private LocalDateTime updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetExchange() {
        return targetExchange;
    }

    public void setTargetExchange(String targetExchange) {
        this.targetExchange = targetExchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MqLocalMsgRecord{" +
        "id=" + id +
        ", targetExchange=" + targetExchange +
        ", routingKey=" + routingKey +
        ", msgContent=" + msgContent +
        ", createTime=" + createTime +
        ", updatedTime=" + updatedTime +
        "}";
    }
}
