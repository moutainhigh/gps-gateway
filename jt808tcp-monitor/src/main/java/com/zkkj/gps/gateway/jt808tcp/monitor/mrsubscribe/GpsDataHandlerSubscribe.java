package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event.GpsDataHandlerEvent;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.ProtocolHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Gps数据处理订阅服务
 * @author suibozhuliu
 */
@Slf4j
@Component
public class GpsDataHandlerSubscribe {

    @Autowired
    private ProtocolHandlerService protocolHandlerService;

    public GpsDataHandlerSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void subscribe(GpsDataHandlerEvent arg) {
        protocolHandlerService.terminalResponse(arg.getProtocolId(), arg.getMsgRequest());
    }

}
