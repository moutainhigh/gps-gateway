package com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum;

public enum GraphTypeEnum implements IBaseEnum {
    //TODO 定义区域类型
    CIRCLE(1, "圆"),
    POLYGON(2, "多边形"),
    LINESTRING(3, "线");

    private final String desc;

    private final int seq;

    GraphTypeEnum(int seq, String desc) {
        this.seq = seq;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public int getSeq() {
        return seq;
    }
}
