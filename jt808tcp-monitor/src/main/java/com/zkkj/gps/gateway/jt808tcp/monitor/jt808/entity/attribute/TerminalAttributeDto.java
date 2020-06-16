package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute;

import lombok.Data;

/**
 * 终端属性模型
 * @author suibozhuliu
 */
@Data
public class TerminalAttributeDto {
    /**
     * 终端类型
     */
    private Integer type;
    /**
     * 制造商ID,终端制造商编码
     */
    private String manufacturerId;
    /**
     * 终端型号,由制造商自行定义,位数不足八位补空格
     */
    private String terminalType;
    /**
     * 终端ID,由大写字母和数字组成,此终端ID由制造商自行定义
     */
    private String terminalId;
    /**
     * 终端SIM卡ICCID
     */
    private String simId;
    /**
     * 硬件版本号长度
     */
    private Integer hardwareVersionLen;
    /**
     * 硬件版本号
     */
    private String hardwareVersion;
    /**
     * 固件版本号长度
     */
    private String firmwareVersionLen;
    /**
     * 固件版本号
     */
    private String firmwareVersion;
    /**
     * GNSS模块属性
     */
    private Integer gnssAttribute;
    /**
     * 通信模块属性
     */
    private Integer networkAttribute;

}
