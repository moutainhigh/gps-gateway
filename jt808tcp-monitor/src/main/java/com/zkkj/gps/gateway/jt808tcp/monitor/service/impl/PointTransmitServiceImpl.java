package com.zkkj.gps.gateway.jt808tcp.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zkkj.gps.gateway.jt808tcp.monitor.entity.response.LocationBean;
import com.zkkj.gps.gateway.jt808tcp.monitor.server.udp.UdpClient;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.PointTransmitService;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 定位数据转发实现
 * @author suibozhuliu
 */
@Service
@Slf4j
public class PointTransmitServiceImpl implements PointTransmitService {

    @Autowired
    private UdpClient udpClient;

    @Override
    public void pointTransmit(LocationBean location) {
        try {
            String locationJson = JSON.toJSONString(location, SerializerFeature.WriteMapNullValue);
            if (!StringUtils.isEmpty(locationJson)) {
                //LoggerUtils.info(log,location.getTerminalId(),"定位数据转发JSON数据：【" + locationJson + "】");
                udpClient.sendMessage(locationJson);
            }
        } catch (Exception e) {
            log.error("定位更新数据发送出现异常：【" + e + "】");
        }
    }
}
