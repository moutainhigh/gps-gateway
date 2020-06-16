package com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum;

/**
 * author : cyc
 * Date : 2019/8/29
 */
public enum AlarmTypeEnum implements IBaseEnum {

    NO_MONITOR(0,"非监控区域"),
    OFF_LINE(1, "掉线"),
    OVER_SPEED(2, "超速"),
    STOP_OVER_TIME(4, "停车超时"),
    VIOLATION_AREA(8, "违规区域"),
    VEHICLE_LOAD(16, "车辆载重"),
    EQUIP_REMOVE(32, "设备拆除"),
    LINE_OFFSET(64, "线路偏移"),
    LOW_POWER(128, "低电量报警"),
    EQUIP_EX(2048, "其他设备异常");

    private final String desc;
    private final int seq;

    AlarmTypeEnum(int seq, String desc) {
        this.desc = desc;
        this.seq = seq;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public int getSeq() {
        return this.seq;
    }
}
