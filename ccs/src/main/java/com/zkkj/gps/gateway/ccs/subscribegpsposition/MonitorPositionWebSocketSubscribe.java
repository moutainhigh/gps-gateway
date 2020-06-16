package com.zkkj.gps.gateway.ccs.subscribegpsposition;

import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.WebSocketDispatchInfo;
import com.zkkj.gps.gateway.ccs.enume.WebSocketTypeEnum;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.websocket.WebSocketMonitorPosition;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.GpsMonitorWebSocketEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Gps通知websocket通知推送
 */
@Component
public class MonitorPositionWebSocketSubscribe {

    private Logger logger = LoggerFactory.getLogger(MonitorPositionWebSocketSubscribe.class);

    public MonitorPositionWebSocketSubscribe() {
        EventBus.getDefault().register(this);
    }

    @Autowired
    private WebSocketMonitorPosition webSocketMonitorPosition;

    @Subscribe
    public void subscribe(GpsMonitorWebSocketEvent gpsMonitorWebSocketEvent) {
        try {
            if (gpsMonitorWebSocketEvent != null && gpsMonitorWebSocketEvent.getTerminalId() != null && gpsMonitorWebSocketEvent.getTerminalId().length() > 0
                    && gpsMonitorWebSocketEvent.getBaseGPSPositionDto() != null && gpsMonitorWebSocketEvent.getBaseGPSPositionDto().getPoint() != null) {
                WebSocketDispatchInfo webSocketDispatchInfo = new WebSocketDispatchInfo();
                Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
                if(dispatchInfoDtoMap.get(gpsMonitorWebSocketEvent.getTerminalId()) != null){
                    webSocketDispatchInfo.setWebSocketDispatchType(WebSocketTypeEnum.ORDINARY.name());
                    webSocketDispatchInfo.setDispatchInfoDto(dispatchInfoDtoMap.get(gpsMonitorWebSocketEvent.getTerminalId()));
                    webSocketMonitorPosition.sendMessage(webSocketDispatchInfo);
                }
            }
        } catch (Exception e) {
            logger.error("MonitorPositionWebSocketSubscribe.subscribe is error", e);
        }
    }


}
