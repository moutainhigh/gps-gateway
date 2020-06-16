package com.zkkj.gps.gateway.automation.service;

import com.zkkj.gps.gateway.automation.entity.ResultVo;
import com.zkkj.gps.gateway.automation.entity.terminalargs.TerminalParams;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

public interface TerminalOptionsService {

    /**
     * 获取终端参数
     *
     * @param terminalId
     * @return
     */
    @RequestLine("POST /readTerminalArgs?terminalId={terminalId}")
    ResultVo<TerminalParams> readTerminalArgs(@Param("terminalId") String terminalId);

    /**
     * 设置终端参数
     *
     * @param terminalId
     * @param terminalParams
     * @return
     */
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST /setTerminalArgs?terminalId={terminalId}")
    ResultVo<String> setTerminalArgs(@Param("terminalId") String terminalId, @RequestBody TerminalParams terminalParams);

}
