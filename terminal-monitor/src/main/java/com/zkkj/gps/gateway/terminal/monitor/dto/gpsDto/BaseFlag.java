package com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto;

import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/30
 * 数据来源标识
 */

@Data
public class BaseFlag implements Serializable {

    /**
     * 电子运单标记；携带的电子运单（0(手机端)：电子运单不存在；1：航宏达；2：自定义；3：甲天行 4：基站定位；5：中交兴路,9测试）
     */
    private int flag;
}
