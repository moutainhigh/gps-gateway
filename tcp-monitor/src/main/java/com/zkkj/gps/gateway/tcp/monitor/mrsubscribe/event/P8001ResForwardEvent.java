package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 8001平台统一响应事件
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-04-23 上午 10:17
 */
@Data
@Component
public class P8001ResForwardEvent {

    private String terminalId;
    private int msgID;

}
