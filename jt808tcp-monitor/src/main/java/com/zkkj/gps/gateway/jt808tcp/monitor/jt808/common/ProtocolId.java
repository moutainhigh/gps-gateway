package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.common;

/**
 * 协议Id枚举类
 * @author suibozhuliu
 */
public enum  ProtocolId {
    C0x0001(0x0001,"终端通用应答"),
    C0x8001(0x8001,"平台通用应答"),
    C0x0002(0x0002,"终端心跳"),
    C0x0100(0x0100,"终端注册"),
    C0x8100(0x8100,"终端注册应答"),
    C0x0003(0x0003,"终端注销"),
    C0x0102(0x0102,"终端鉴权"),
    C0x8103(0x8103,"设置终端参数"),
    C0x0104(0x0104,"查询终端参数应答"),
    C0x8106(0x8106,"查询指定终端参数"),
    C0x8107(0x8107,"查询终端属性"),
    C0x0107(0x0107,"查询终端属性应答"),
    C0x0200(0x0200,"位置信息汇报"),
    C0x8201(0x8201,"位置信息查询"),
    C0x0201(0x0201,"位置信息查询应答"),
    C0x0701(0x0701,"电子运单上报"),
    C0x0704(0x0704,"定位数据批量上传"),
    C0x8900(0x8900,"数据下行透传"),
    C0x0900(0x0900,"数据上行透传");

    public final int value;
    public final String desc;

    ProtocolId(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ProtocolId toEnum(int value) {
        for (ProtocolId type : ProtocolId.values())
            if (value == type.value)
                return type;
        return null;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
