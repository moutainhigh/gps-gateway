package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;

import java.util.List;

/**
 * 消息队列第三方用户属性缓存接口
 */
public interface AmqpService {

    Integer addAmqpUser(AmqpVerifyDto amqpVerifyDto) throws Exception;

    List<AmqpVerifyDto> getAmqpUser() throws Exception;

    Integer updateAmqpUser (AmqpVerifyDto amqpVerifyDto) throws Exception;

}
