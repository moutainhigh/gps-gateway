package com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params;

import lombok.Data;

/**
 * 终端参数对外模型
 * @author suibozhuliu
 */
@Data
public class TerminalParamsDto {

    /**
     * 终端心跳发送间隔，单位为秒(s)
     */
    private int heartPeriod;

    /**
     * TCP 消息应答超时时间，单位为秒(s)
     */
    private int tcpResponseTime;

    /**
     * 主服务器地址，IP 或域名
     */
    private String mainHost;

    /**
     * 备份服务器地址，IP 或域名
     */
    private String backupsHost;

    /**
     * 主服务器端口
     */
    private int mainPort;

    /**
     * 备份服务器端口
     */
    private int backupsPort;

    /**
     * 机动车牌号
     */
    private String plateNumber;


}
