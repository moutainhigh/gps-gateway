package com.zkkj.gps.gateway.ccs.dto.amqp;

import lombok.Data;

/**
 * 向第三方推送消息模型
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-25 下午 8:00
 */
@Data
public class PushMsgDto<T> {

    private String appKey;
    private String appSecret;
    private T data;

}
