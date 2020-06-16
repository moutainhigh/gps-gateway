package com.zkkj.gps.gateway.tcp.monitor.app.service;

import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.SyncBean;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 甲天行远程Api服务（铜川大区）
 * @author suibozhuliu
 */
public interface JtxJt1ApiService {

    /**
     * 甲天行下发电子运单
     *
     * @param terminalId
     * @param businessData
     * @return
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /setBusiness?terminalId={terminalId}")
    SyncBean<String> setBusiness(@Param("terminalId") String terminalId, @RequestBody BusinessBean businessData);

    /**
     * 甲天行读取电子运单
     *
     * @param terminalId
     * @return
     */
    @RequestLine("GET /readBusiness?terminalId={terminalId}")
    SyncBean<BusinessBean> readBusiness(@Param("terminalId") String terminalId);

    /**
     * 甲天行设备下发电子车牌
     *
     * @param terminalId
     * @param plateNum
     * @return
     */
    @RequestLine("POST /issuePlateNum?terminalId={terminalId}&plateNum={plateNum}")
    SyncBean<String> issuePlateNum(@Param("terminalId") String terminalId, @Param("plateNum") String plateNum);

    /**
     * 甲天行设备读取电子车牌
     *
     * @param terminalId
     * @return
     */
    @RequestLine("GET /readPlateNum?terminalId={terminalId}")
    SyncBean<String> readPlateNum(@Param("terminalId") String terminalId);

}
