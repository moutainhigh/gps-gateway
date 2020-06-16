package com.zkkj.gps.gateway.ccs.subscribegpsposition;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.WebSocketDispatchInfo;
import com.zkkj.gps.gateway.ccs.websocket.WebSocketMonitorPosition;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author : cyc
 * Date : 2020/5/11
 * 任务新增或者移除订阅
 */

@Component
public class TaskMonitorSubscribe {

    @Autowired
    private WebSocketMonitorPosition webSocketMonitorPosition;

    private Logger logger = LoggerFactory.getLogger(TaskMonitorSubscribe.class);

    public TaskMonitorSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(WebSocketDispatchInfo webSocketDispatchInfo) {
        try {
            if (webSocketDispatchInfo != null) {
                webSocketMonitorPosition.sendMessage(webSocketDispatchInfo);
            }
        } catch (Exception e) {
            logger.error("TaskMonitorSubscribe.subscribe is error", e);
        }
    }

}
