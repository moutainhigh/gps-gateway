package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;

/**
 * 车辆派车信息验证服务
 * @author suibozhuliu
 */
public interface DispatchVerifyService {

    /**
     * 根据终端编号验证设备是否在线
     * @param terminalNo
     * @return
     */
    boolean terminalOnLineVerify(String terminalNo);

    /**
     * 车辆是否在指定区域验证
     * @param terminalNo
     * @return
     */
    boolean terminalInAreaVerify(String terminalNo,InAreaDto inAreaDto);

}
