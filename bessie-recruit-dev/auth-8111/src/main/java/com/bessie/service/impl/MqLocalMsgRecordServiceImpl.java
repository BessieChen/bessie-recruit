package com.bessie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bessie.mapper.MqLocalMsgRecordMapper;
import com.bessie.pojo.MqLocalMsgRecord;
import com.bessie.service.MqLocalMsgRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bessie
 * @since 2023-07-13
 */
@Service
public class MqLocalMsgRecordServiceImpl extends ServiceImpl<MqLocalMsgRecordMapper, MqLocalMsgRecord> implements MqLocalMsgRecordService {

    @Autowired
    private MqLocalMsgRecordMapper msgRecordMapper;

    @Override
    public List<MqLocalMsgRecord> getBatchLocalMsgRecordList(List<String> msgIds) {
        return msgRecordMapper.selectBatchIds(msgIds);
    }

}
