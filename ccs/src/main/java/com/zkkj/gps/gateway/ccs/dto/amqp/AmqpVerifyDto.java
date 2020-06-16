package com.zkkj.gps.gateway.ccs.dto.amqp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方用户请求验证消息队列初始化模型
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-25 下午 7:47
 */
@Data
public class AmqpVerifyDto {
    @ApiModelProperty(value = "appKey", name = "appKey")
    private String appKey;
    @ApiModelProperty(value = "appSecret", name = "appSecret")
    private String appSecret;
    @ApiModelProperty(value = "第三方消息接收服务Url", name = "thirdPartyUrl")
    private String thirdPartyUrl;
}
