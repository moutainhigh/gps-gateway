package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;
import com.zkkj.gps.gateway.ccs.mappper.AmqpMapper;
import com.zkkj.gps.gateway.ccs.service.AmqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息队列第三方用户属性缓存接口实现
 */
@Service
public class AmqpServiceImpl implements AmqpService {

    @Autowired
    private AmqpMapper amqpMapper;

    @Override
    public Integer addAmqpUser(AmqpVerifyDto amqpVerifyDto) throws Exception {
        return amqpMapper.addAmqpUser(amqpVerifyDto);
    }

    @Override
    public List<AmqpVerifyDto> getAmqpUser() throws Exception {
        return amqpMapper.getAmqpUser();
    }

    @Override
    public Integer updateAmqpUser(AmqpVerifyDto amqpVerifyDto) throws Exception {
        return amqpMapper.updateAmqpUser(amqpVerifyDto);
    }
}
