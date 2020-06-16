package com.zkkj.gps.gateway.jt808tcp.monitor.entity.response;

import lombok.Data;
import lombok.ToString;

/**
 * 定位信息模型（附加）
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class PositionBean extends PositionBaseBean {

    /**
     * 报警标志 4
     * [1] 1：超速报警
     * [2] 1：疲劳驾驶
     * [4] 1：GNSS模块发生故障
     * [5] 1：GNSS天线未接或被断路
     * [6] 1：GNSS天线短路
     * [7] 1：终端主电源欠压
     * [8] 1：终端主电源掉电
     * [19] 1：超时停车
     * 其他位保留
     */
    private long alarmState;
    /**
     * 状态定义 4
     */
    private long terminalState;
    /**
     * 载重传感器压力值
     */
    private Double loadSensorValue;
    /**
     * 是否存在载重传感器
     */
    private boolean loadSensorIsExist;
    /**
     * Acc状态 0: ACC关；1:ACC开
     */
    private int acc;
    /**
     * 油量 2 1-10L
     */
    private int oilMass;
    /**
     * 电量值 3 400 (毫伏,相当于4.00V)
     */
    private String power;
    /**
     * 防拆开关 1 0:正常 1:被拆触发
     */
    private String antiDismantle;

}

