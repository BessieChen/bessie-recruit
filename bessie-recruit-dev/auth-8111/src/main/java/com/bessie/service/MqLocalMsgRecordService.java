package com.bessie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bessie.pojo.MqLocalMsgRecord;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bessie
 * @since 2023-07-13
 */
public interface MqLocalMsgRecordService extends IService<MqLocalMsgRecord> {

    /**
     * 批量根据id获得记录列表
     * @param msgIds
     * @return
     */
    public List<MqLocalMsgRecord> getBatchLocalMsgRecordList(List<String> msgIds);

}
