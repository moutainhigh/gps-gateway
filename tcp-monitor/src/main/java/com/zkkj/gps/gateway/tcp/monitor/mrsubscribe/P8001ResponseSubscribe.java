package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.tcp.monitor.app.api.ViewActionImpl;
import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.P8001ResForwardEvent;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用应答响应服务
 */
@Slf4j
@Component
public class P8001ResponseSubscribe {

    @Autowired
    private ViewActionImpl viewActionImpl;

    public P8001ResponseSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(P8001ResForwardEvent arg) {
        try {
            viewActionImpl.platformResponse(arg.getTerminalId(), arg.getMsgID());
        } catch (Exception e) {
            if (arg != null && arg.getTerminalId() != null && !arg.getTerminalId().equals("")) {
                LoggerUtils.error(log,arg.getTerminalId(),"8001平台统一响应异常：" + e.toString());
            }
        }
    }
}
