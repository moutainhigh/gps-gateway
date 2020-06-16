package com.zkkj.gps.gateway.ccs.subscribegpsposition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zkkj.gps.gateway.ccs.websocket.WebSocketPosition;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsWebSocketEvent;

/**
 * Gps通知websocket通知推送
 */
@Component
public class GPSPositionWebSocketSendSubscribe {

    private Logger logger = LoggerFactory.getLogger(GPSPositionWebSocketSendSubscribe.class);

    public GPSPositionWebSocketSendSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Autowired
    private WebSocketPosition webSocketPosition;

    @Subscribe
    public void subscribe(GpsWebSocketEvent gpsWebSocketEvent) {
        try {
            if (gpsWebSocketEvent != null && gpsWebSocketEvent.getTerminalId() != null && gpsWebSocketEvent.getTerminalId().length() > 0
                    && gpsWebSocketEvent.getBaseGPSPositionDto() != null && gpsWebSocketEvent.getBaseGPSPositionDto().getPoint() != null) {
                webSocketPosition.sendMessage(gpsWebSocketEvent.getTerminalId());
            }
        } catch (Exception e) {
            logger.error("GPSPositionWebSocketSendSubscribe.subscribe is error", e);
        }
    }


}
