package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.TerminalParamsInfoDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author : cyc
 * Date : 2019-06-12
 * gps车载设备接口
 */
public interface GpsWebApiService {

    /**
     * 下发电子运单
     *
     * @param terminalType
     * @param terminalId
     * @param gpsBusinessDto
     * @return
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /setBusiness?terminalType={terminalType}&terminalId={terminalId}")
    ResultVo<String> setBusiness(@Param("terminalType") int terminalType, @Param("terminalId") String terminalId, @RequestBody GpsBusinessDto gpsBusinessDto);

    /**
     * 读取电子运单
     *
     * @param terminalType
     * @param terminalId
     * @return
     */
    @RequestLine("POST /readBusiness?terminalType={terminalType}&terminalId={terminalId}")
    ResultVo<GpsBusinessDto> readBusiness(@Param("terminalType") int terminalType, @Param("terminalId") String terminalId);

    /**
     * 设置终端参数
     *
     * @param terminalId
     * @param terminalParamsInfoDto
     * @return
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /setTerminalArgs?terminalId={terminalId}")
    ResultVo<String> setTerminalArgs(@Param("terminalId") String terminalId, @RequestBody TerminalParamsInfoDto terminalParamsInfoDto);

    /**
     * 获取终端参数
     *
     * @param terminalId
     * @return
     */
    @RequestLine("POST /readTerminalArgs?terminalId={terminalId}")
    ResultVo<TerminalParamsInfoDto> readTerminalArgs(@Param("terminalId") String terminalId);

    /**
     * 蓝牙电子锁施封
     *
     * @param terminalId
     * @return
     */
    @RequestLine("POST /closeLock?terminalId={terminalId}")
    ResultVo<String> closeLock(@Param("terminalId") String terminalId);

    /**
     * 蓝牙电子锁解封
     *
     * @param terminalId
     * @return
     */
    @RequestLine("POST /openLock?terminalId={terminalId}")
    ResultVo<String> openLock(@Param("terminalId") String terminalId);

    /**
     * 蓝牙设备下发电子车牌
     *
     * @param terminalType
     * @param terminalId
     * @param plateNum
     * @return
     */
    @RequestLine("POST /issuePlateNum?terminalType={terminalType}&terminalId={terminalId}&plateNum={plateNum}")
    ResultVo<String> issuePlateNum(@Param("terminalType") int terminalType, @Param("terminalId") String terminalId, @Param("plateNum") String plateNum);

    /**
     * 蓝牙设备读取电子车牌
     *
     * @param terminalType
     * @param terminalId
     * @return
     */
    @RequestLine("POST /readPlateNum?terminalType={terminalType}&terminalId={terminalId}")
    ResultVo<String> readPlateNum(@Param("terminalType") int terminalType, @Param("terminalId") String terminalId);

    /*//一般的get请求使用url参数清洁
    @RequestLine("GET /hello?arg={arg}")
    ResultVo<String> getHello(@Param("arg") String arg);

    //get请求中使用restful，调用对应的服务也要用restful风格接受
    @RequestLine("GET /hello/{arg}")
    ResultVo<String> getHelloWorld(@Param("arg") String arg);*/
}
