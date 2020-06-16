package com.zkkj.gps.gateway.jt808tcp.monitor.mrsubscribe.event;

import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common.ProtocolId;
import com.zkkj.gps.gateway.jt808tcp.monitor.message.AbstractMessage;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Gps数据处理事件
 * @author suibozhuliu
 */
@Data
@Component
public class GpsDataHandlerEvent {

    private AbstractMessage msgRequest;

    private ProtocolId protocolId;

}
