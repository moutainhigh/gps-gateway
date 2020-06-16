package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common;


import com.zkkj.gps.gateway.jt808tcp.monitor.enums.DataType;

/**
 * 位置追加信息枚举类
 * @author suibozhuliu
 */
public enum AttributeId {

    C0x01(0x01,DataType.DWORD,"里程，DWORD，1/10km，对应车上里程表读数"),
    C0x02(0x02,DataType.WORD,"油量，WORD，1/10L，对应车上油量表读数"),
    C0x03(0x03,DataType.WORD,"行驶记录功能获取的速度，WORD，1/10km/h"),
    C0x2A(0x2A,DataType.WORD,"IO状态位"),
    C0xE1(0xE1,DataType.WORD,"电压 WORD；单位 0.01V "),
    C0x33(0x33,DataType.BYTES,"蓝牙终端数据");

    public final int value;
    public final DataType type;
    public final String desc;

    AttributeId(int value, DataType type, String desc) {
        this.value = value;
        this.type = type;
        this.desc = desc;
    }

    public static AttributeId toEnum(int value) {
        for (AttributeId type : AttributeId.values())
            if (value == type.value)
                return type;
        return null;
    }

    @Override
    public String toString() {
        return this.desc;
    }

}
