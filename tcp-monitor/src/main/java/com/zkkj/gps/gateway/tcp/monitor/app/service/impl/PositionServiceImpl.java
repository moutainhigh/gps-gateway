package com.zkkj.gps.gateway.tcp.monitor.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.LocationBean;
import com.zkkj.gps.gateway.tcp.monitor.app.service.PositionService;
import com.zkkj.gps.gateway.tcp.monitor.socket.udp.UdpClient;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 定位信息更新接口
 *
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-17 上午 11:34
 */
@Service
@Slf4j
public class PositionServiceImpl implements PositionService {

    @Autowired
    private UdpClient udpClient;

    @Override
    public void positionChange(LocationBean location) {
        try {
            String positionStr = JSON.toJSONString(location, SerializerFeature.WriteMapNullValue);
            if (!StringUtils.isEmpty(positionStr)) {
                udpClient.sendMessage(positionStr);
                //LoggerUtils.info(log, location.getTerminalId(), "定位更新数据Json：" + positionStr);
                //LoggerUtils.info(log, location.getTerminalId(), "定位更新数据发送：【" + location.toString() + "】");
            } else {
                //LoggerUtils.error(log, location.getTerminalId(), "定位更新数据发送失败：【定位信息Json数据转换异常】");
            }
        } catch (Exception e) {
            LoggerUtils.error(log, "定位更新数据发送出现异常：" + e);
        }
    }

}
