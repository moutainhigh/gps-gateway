package com.zkkj.gps.gateway.tcp.monitor.app.common;

/**
 * 车牌归属地与字母对照枚举类
 * @author suibozhuliu
 */
public enum CarPlateEnum {

    HJ("冀","HJ"),
    HY("豫","HY"),
    YN("云","YN"),
    LN("辽","LN"),
    HL("黑","HL"),
    HX("湘","HX"),
    AH("皖","AH"),
    SD("鲁","SD"),
    XJ("新","XJ"),
    JS("苏","JS"),
    ZJ("浙","ZJ"),
    JX("赣","JX"),
    HB("鄂","HB"),
    GX("桂","GX"),
    GS("甘","GS"),
    SJ("晋","SJ"),
    NM("蒙","NM"),
    SX("陕","SX"),
    JL("吉","JL"),
    FJ("闽","FJ"),
    GZ("贵","GZ"),
    GD("粤","GD"),
    QH("青","QH"),
    XZ("藏","XZ"),
    SC("川","SC"),
    NX("宁","NX"),
    HN("琼","HN"),
    TW("台","TW"),
    CQ("渝","CQ"),
    BJ("京","BJ"),
    TJ("津","TJ"),
    SH("沪","SH");

    //行政区
    private final String region;
    //标识
    private final String identify;

    CarPlateEnum(String region, String identify) {
        this.region = region;
        this.identify = identify;
    }

    public static String getIdentify(String region) {
        for (CarPlateEnum type : CarPlateEnum.values())
            if (region.equals(type.region)){
                return type.identify;
            }
        return null;
    }

    public static String getRegiony(String identify) {
        for (CarPlateEnum type : CarPlateEnum.values())
            if (identify.equals(type.identify))
                return type.region;
        return null;
    }

    @Override
    public String toString() {
        return "CarPlateEnum{" +
                "region='" + region + '\'' +
                ", identify='" + identify + '\'' +
                '}';
    }

}
