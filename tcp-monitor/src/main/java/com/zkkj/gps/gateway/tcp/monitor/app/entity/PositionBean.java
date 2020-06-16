package com.zkkj.gps.gateway.tcp.monitor.app.entity;

import com.zkkj.gps.gateway.protocol.dto.GPSPositionDTO;
import lombok.Data;

/**
 * 定位信息更新数据对象
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-06-04 上午 10:09
 */
@Data
public class PositionBean {
    /**
     * 终端编号
     */
    private String terminalId;
    /**
     * 位置信息对象
     */
    private GPSPositionDTO positionDTO;

}
