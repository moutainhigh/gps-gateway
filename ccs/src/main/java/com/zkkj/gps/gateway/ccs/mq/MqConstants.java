package com.zkkj.gps.gateway.ccs.mq;

import com.zkkj.gps.gateway.ccs.dto.amqp.AmqpVerifyDto;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列常量类
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-21 上午 10:33
 */
public class MqConstants {

    /**
     * 交换机名称
     */
    public final static String EXCHANGE_NAME = "gis_exchange";

    /**
     * 保存AppKey对应的第三方接口地址和AppSecret
     */
    public static final Map<String,AmqpVerifyDto> appKeyUrlMap = new HashMap<>();

    /**
     * 事件消息接口名称
     */
    public final static String consumerEventMsg = "consumerEventMsg";

    /**
     * 报警消息接口名称
     */
    public final static String consumerAlarmMsg = "consumerAlarmMsg";

}
