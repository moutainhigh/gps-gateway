package com.zkkj.gps.gateway.automation.entity.terminalargs;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-05-14 下午 5:33
 */
@Data
@ApiModel(value = "终端参数模型", description = "终端参数模型")
public class TerminalParams {

    /**
     * 终端序号 6 bcd码
     * 1) 使用手机号
     * 2) 使用终端内的IMEI
     * 3) 使用终端贴牌号
     * 0x01
     */
    @ApiModelProperty(value = "终端序号 6 bcd码:1) 使用手机号;2) 使用终端内的IMEI;" +
            "3) 使用终端贴牌号", name = "terminalId")
    private String terminalId;
    /**
     * 0000：副MCU不关闭主MCU电源；（默认该模式，长连接模式，一直在线，保持一直在线）
     * 关闭主MCU分钟数（非在线模式，会自动采用短连接方式与平台交互信息）
     * 0x02
     */
    @ApiModelProperty(value = "0000：副MCU不关闭主MCU电源；（默认该模式，长连接模式，一直在线，保持一直在线）" +
            "关闭主MCU分钟数（非在线模式，会自动采用短连接方式与平台交互信息）", name = "mcuTime")
    private int mcuTime;
    /**
     * 主服务器IP
     * 0x03
     */
    @ApiModelProperty(value = "主服务器IP", name = "mainIp")
    private String mainIp;
    /**
     * 备份服务器IP
     * 0x04
     */
    @ApiModelProperty(value = "备份服务器IP", name = "backupIp")
    private String backupIp;
    /**
     * 服务器TCP端口
     * 0x05
     */
    @ApiModelProperty(value = "服务器TCP端口", name = "port")
    private String port;
    /**
     * 定时汇报间隔，单位为秒（s），>0
     * 0x06
     */
    @ApiModelProperty(value = "定时汇报间隔，单位为秒（s），>0", name = "reportTime")
    private int reportTime;
    /**
     * 定位模式，定义如下：
     * bit0，0：禁用GPS定位， 1：启用GPS定位；
     * bit1，0：禁用北斗定位， 1：启用北斗定位。
     * 0x07
     */
    @ApiModelProperty(value = "定位模式，定义如下：bit0，0：禁用GPS定位， 1：启用GPS定位；" +
            "bit1，0：禁用北斗定位， 1：启用北斗定位。", name = "locationMode")
    private int locationMode;
    /**
     * 终端心跳发送间隔，单位为秒（s）
     * 0x08
     */
    @ApiModelProperty(value = "终端心跳发送间隔，单位为秒（s）", name = "palmusTime")
    private int palmusTime;
    /**
     * APN名称（SIM卡1）
     * 0x09
     */
    @ApiModelProperty(value = "APN名称（SIM卡1）", name = "apnName")
    private String apnName;
    /**
     * APN用户名（SIM卡1）
     * 0x0A
     */
    @ApiModelProperty(value = "APN用户名（SIM卡1）", name = "apnUserName")
    private String apnUserName;
    /**
     * APN密码（SIM卡1）
     * 0x0B
     */
    @ApiModelProperty(value = "APN密码（SIM卡1）", name = "apnPassword")
    private String apnPassword;
    /**
     * APN名称（SIM卡2）
     * 0x0C
     */
    @ApiModelProperty(value = "APN名称（SIM卡2）", name = "apnName2")
    private String apnName2;
    /**
     * APN用户名（SIM卡2）
     * 0x0D
     */
    @ApiModelProperty(value = "APN用户名（SIM卡2）", name = "apnUserName2")
    private String apnUserName2;
    /**
     * APN密码（SIM卡2）
     * 0x0E
     */
    @ApiModelProperty(value = "APN密码（SIM卡2）", name = "apnPassword2")
    private String apnPassword2;
    /**
     * 卡模式切换：01：只用卡1   02：只用卡2  03：卡1优先  04：卡2优先
     * 0x0F
     */
    @ApiModelProperty(value = "卡模式切换：01：只用卡1；   02：只用卡2；  03：卡1优先；  04：卡2优先", name = "simMode")
    private int simMode;
    /**
     * 3G/2G模式切换：00：两次2G联不上，转3G；   01：只用2G；  02：只用3G  03：2G联不上转3G   04：优先使用3G
     * 0x10
     */
    @ApiModelProperty(value = "3G/2G模式切换：00：两次2G联不上，转3G；   01：只用2G；  " +
            "02：只用3G；  03：2G联不上转3G；   04：优先使用3G", name = "netMode")
    private int netMode;
    /**
     * 终端后备电池告警电压值，单位为10MV
     * 0x11
     */
    @ApiModelProperty(value = "终端后备电池告警电压值，单位为10MV", name = "warnVoltage")
    private int warnVoltage;
    /**
     * IC卡访问密码（12字节）
     * 0x12
     */
    @ApiModelProperty(value = "IC卡访问密码（12字节）", name = "icPassword")
    private String icPassword;
    /**
     * SMS访问密码（8字节）
     * 0x13
     */
    @ApiModelProperty(value = "SMS访问密码（8字节）", name = "smsPassword")
    private String smsPassword;
    /**
     * 振动开关振动检测时间，单位为分钟
     * 0x14
     */
    @ApiModelProperty(value = "振动开关振动检测时间，单位为分钟", name = "shakeTime")
    private int shakeTime;
    /**
     * 实时时钟时间（在无GPS功能时校准用）
     * 0x15
     */
    @ApiModelProperty(value = "实时时钟时间（在无GPS功能时校准用）", name = "currentTime")
    private String currentTime;
    /**
     * 00：不启用自动施封
     * 01: ACC启动后，自动启用施封
     * 02：振动生效后，自动启用施封
     * 03：ACC启动或者振动生效后，自动启用施封
     * 0x16
     */
    @ApiModelProperty(value = "00：不启用自动施封；01: ACC启动后，自动启用施封；02：振动生效后，自动启用施封；" +
            "03：ACC启动或者振动生效后，自动启用施封", name = "selfLock")
    private int selfLock;
    /**
     * 子锁1ID
     * 0x17
     */
    //private String childLockId1;
    /**
     * 子锁2ID
     * 0x18
     */
    //private String childLockId2;
    /**
     * 子锁3ID
     * 0x19
     */
    //private String childLockId3;
    /**
     * 子锁4ID
     * 0x1A
     */
    //private String childLockId4;
    /**
     * 子锁5ID
     * 0x1B
     */
    //private String childLockId5;
    /**
     * 子锁6ID
     * 0x1C
     */
    //private String childLockId6;
    /**
     * 子锁7ID
     * 0x1D
     */
    //private String childLockId7;
    /**
     * 子锁8ID
     * 0x1E
     */
    //private String childLockId8;
    /**
     * 子锁9ID
     * 0x1F
     */
    //private String childLockId9;
    /**
     * 子锁10ID
     * 0x20
     */
    //private String childLockId10;
    /**
     * 子锁11ID
     * 0x21
     */
    //private String childLockId11;
    /**
     * 子锁12ID
     * 0x22
     */
    //private String childLockId12;
    /**
     * 01：清除所有区域及刷卡的使用规则
     * 02:清除历史轨迹数据
     * 03:恢复出厂设置
     * 04:终端强制重启
     * 05:点名触发获取一次位置汇报
     * 06:点名触发获取一次位置汇报(带业务扩展数据)
     * 0X23
     */
    @ApiModelProperty(value = "01：清除所有区域及刷卡的使用规则；02:清除历史轨迹数据；03:恢复出厂设置；04:终端强制重启；" +
            "05:点名触发获取一次位置汇报；06: 点名触发获取一次位置汇报(带业务扩展数据)", name = "terminalAction")
    private int terminalAction;
    /**
     * 保留1
     * 0x24
     */
    //private int reserve1;
    /**
     * 子锁序号从01至12共计允许使用12个子锁，每个子锁如果对应的BIT位为0表示该子锁是被允许解封开锁，为1则表示该子锁被禁止解封开锁操作。
     * 如0X0123：变为二进制位，只需看后12个BIT位，即：0001  0010  0011（子锁1，2，6，9被禁止解封开锁，余下的子锁3，4，5，7，8，10，11，12允许解封开锁。）
     * 0X25
     */
    @ApiModelProperty(value = "子锁序号从01至12共计允许使用12个子锁，每个子锁如果对应的BIT位为0表示该子锁是被允许解封开锁，为1则表示该子锁被禁止解封开锁操作。" +
            "如0X0123：变为二进制位，只需看后12个BIT位，即：0001  0010  0011（子锁1，2，6，9被禁止解封开锁，余下的子锁3，4，5，7，8，10，11，12允许解封开锁。）",
            name = "isUnlocking")
    private int isUnlocking;
    /**
     * 按键应急密码解封开锁（子锁均可以同时被解封），带蓝牙功功或者按键密码可使用本功能。
     * 蓝牙密码
     * 0X26
     */
    @ApiModelProperty(value = "按键应急密码解封开锁（蓝牙密码）", name = "emergencyUnlock")
    private String emergencyUnlock;
    /**
     * 保留
     * 0X27
     */
    //private int reserve2;
    /**
     * 临时IC卡号1
     * 0X28
     */
    @ApiModelProperty(value = "临时IC卡号1", name = "tempIcCardId1")
    private String tempIcCardId1;
    /**
     * 临时IC卡号2
     * 0X29
     */
    @ApiModelProperty(value = "临时IC卡号2", name = "tempIcCardId2")
    private String tempIcCardId2;
    /**
     * 临时IC卡号3
     * 0X2A
     */
    @ApiModelProperty(value = "临时IC卡号3", name = "tempIcCardId3")
    private String tempIcCardId3;
    /**
     * 临时IC卡号4
     * 0X2B
     */
    @ApiModelProperty(value = "临时IC卡号4", name = "tempIcCardId4")
    private String tempIcCardId4;
    /**
     * 蓝牙设备名称更改（ASSIC）不足填充0x00
     * 0x35
     */
    @ApiModelProperty(value = "蓝牙设备名称更改（ASSIC）不足填充0x00", name = "bluetoothName")
    private String bluetoothName;
    /**
     * 更改终端蓝牙的连接密码(ASSIC)不足填充0x00
     * 0x36
     */
    @ApiModelProperty(value = "更改终端蓝牙的连接密码(ASSIC)不足填充0x00", name = "bluetoothPassword")
    private String bluetoothPassword;


}
