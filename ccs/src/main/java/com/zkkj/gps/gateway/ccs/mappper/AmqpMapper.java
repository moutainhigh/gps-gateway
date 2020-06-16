package com.zkkj.gps.gateway.ccs.mappper;

import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息队列第三方属性持久
 */
@Mapper
public interface AmqpMapper {

    Integer addAmqpUser(AmqpVerifyDto amqpVerifyDto) throws Exception;

    List<AmqpVerifyDto> getAmqpUser() throws Exception;

    Integer updateAmqpUser (AmqpVerifyDto amqpVerifyDto) throws Exception;
}
