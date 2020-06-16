package com.zkkj.gps.gateway.jt808tcp.monitor.service;

import com.zkkj.gps.gateway.jt808tcp.monitor.entity.ResultVo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute.TerminalAttributeDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params.TerminalParamsDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;

/**
 * 协议下行业务接口
 * @author suibozhuliu
 */
public interface ProtocolIssueService {

    /**
     * 读取终端参数
     * @param terminalId
     * @return
     */
    ResultVo<TerminalParamsDto> getParameters(String terminalId);

    /**
     * 终端参数设置
     * @param terminalId
     * @param terminalParamsDto
     * @return
     */
    ResultVo<Boolean> setParameters(String terminalId,TerminalParamsDto terminalParamsDto);

    /**
     * 读取设备车牌
     * @param terminalId
     * @return
     */
    ResultVo<String> getDeviceCarNum(String terminalId);

    /**
     * 读取电子车牌
     * @param terminalId
     * @return
     */
    ResultVo<String> readPlateNum(String terminalId);

    /**
     * 下发设备车牌
     * @param terminalId
     * @param plateNumber
     * @return
     */
    ResultVo<Boolean> setDeviceCarNum(String terminalId,String plateNumber);

    /**
     * 下发电子车牌
     * @param terminalId
     * @param plateNumber
     * @return
     */
    ResultVo<String> issuePlateNum(String terminalId,String plateNumber);

    /**
     * 查询终端属性
     * @param terminal
     * @return
     */
    ResultVo<TerminalAttributeDto> getAttribute(String terminal);

    /**
     * 电子运单查询
     * @param terminalId
     * @return
     */
    ResultVo<ElecDispatchInfo> readBussiness(String terminalId);

    /**
     * 电子运单下发
     * @param terminalId
     * @param elecDispatchInfo
     * @return
     */
    ResultVo<String> setBusiness(String terminalId,ElecDispatchInfo elecDispatchInfo);

}
