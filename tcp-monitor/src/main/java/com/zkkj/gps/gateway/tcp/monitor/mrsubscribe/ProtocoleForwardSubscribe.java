package com.zkkj.gps.gateway.tcp.monitor.mrsubscribe;

import com.zkkj.gps.gateway.protocol.component.common.DestinationBaseCompose;
import com.zkkj.gps.gateway.protocol.destination.*;
import com.zkkj.gps.gateway.tcp.monitor.app.api.ViewActionImpl;
import com.zkkj.gps.gateway.tcp.monitor.mrsubscribe.event.ProtocoleForwardEvent;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


/**
 * 协议转发订阅服务
 */
@Slf4j
@Component
public class ProtocoleForwardSubscribe {

    @Autowired
    private ViewActionImpl viewActionImpl;

    public ProtocoleForwardSubscribe(){
        EventBus.getDefault().register(this);
    }

    //异步处理
    @Subscribe
    public void subscribe(ProtocoleForwardEvent arg) {
        try {
            if (arg != null) {
                //消息ID说明
                String msgIdStr = arg.getMsgIdStr();
                //响应字节数组
                byte[] resByte = arg.getResByte();
                //协议解析父对象
                DestinationBaseCompose compose = null;
                switch (msgIdStr) {
                    case "0200":
                        compose = new P_0200(resByte);
                        break;
                    case "0210":
                        compose = new P_0210(resByte);
                        break;
                    case "0311":
                        compose = new P_0311(resByte);
                        break;
                    case "0313":
                        compose = new P_0313(resByte);
                        break;
                    case "0315":
                        compose = new P_0315(resByte);
                        break;
                    case "0317":
                        compose = new P_0317(resByte);
                        break;
                    case "0319":
                        compose = new P_0319(resByte);
                        break;
                    case "0411":
                        compose = new P_0411(resByte);
                        break;
                }
                if (!ObjectUtils.isEmpty(compose) && !ObjectUtils.isEmpty(compose.getMessageHeader())) {
                    //响应结果回调
                    viewActionImpl.responseCallBack(compose, compose.getMessageHeader());
                }
            }
        } catch (Exception e) {
            /*if (!ObjectUtils.isEmpty(arg) && !StringUtils.isEmpty(arg.getMsgIdStr())) {
                LoggerUtils.error(log, arg.getMsgIdStr(), "终端数据解析异常：【" + e + "】");
            } else {
                LoggerUtils.error(log, e);
            }*/
        }
    }

}
