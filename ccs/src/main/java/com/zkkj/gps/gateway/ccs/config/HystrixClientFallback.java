package com.zkkj.gps.gateway.ccs.config;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.TerminalParamsInfoDto;
import com.zkkj.gps.gateway.ccs.service.GpsWebApiService;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * author : cyc
 * Date : 2019/10/15
 */
@Component
public class HystrixClientFallback implements FallbackFactory<GpsWebApiService> {

    private Logger logger = LoggerFactory.getLogger(HystrixClientFallback.class);

    @Override
    public GpsWebApiService create(Throwable throwable) {

        logger.error("HystrixClientFallback.throwable is error", throwable);
        return new GpsWebApiService() {
            @Override
            public ResultVo<String> setBusiness(int terminalType, String terminalId, GpsBusinessDto gpsBusinessDto) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.setBusiness terminalType:" + terminalType + ";terminal:" + terminalId + ";gpsBusinessDto" + FastJsonUtils.toJSONString(gpsBusinessDto));
                return resultVo;
            }

            @Override
            public ResultVo<GpsBusinessDto> readBusiness(int terminalType, String terminalId) {
                ResultVo<GpsBusinessDto> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.readBusiness terminalType:" + terminalType + ";terminalId:" + terminalId);
                return resultVo;
            }

            @Override
            public ResultVo<String> setTerminalArgs(String terminalId, TerminalParamsInfoDto terminalParamsInfoDto) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.setTerminalArgs terminal:" + terminalId + ";terminalParamsInfoDto:" + FastJsonUtils.toJSONString(terminalParamsInfoDto));
                return resultVo;
            }

            @Override
            public ResultVo<TerminalParamsInfoDto> readTerminalArgs(String terminalId) {
                ResultVo<TerminalParamsInfoDto> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.readTerminalArgs terminal:" + terminalId);
                return resultVo;
            }

            @Override
            public ResultVo<String> closeLock(String terminalId) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.closeLock terminal:" + terminalId);
                return resultVo;
            }

            @Override
            public ResultVo<String> openLock(String terminalId) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.openLock terminal:" + terminalId);
                return resultVo;
            }

            @Override
            public ResultVo<String> issuePlateNum(int terminalType, String terminalId, String plateNum) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.issuePlateNum terminalType:" + terminalType + ";terminalId:" + terminalId + ";plateNum:" + plateNum);
                return resultVo;
            }

            @Override
            public ResultVo<String> readPlateNum(int terminalType, String terminalId) {
                ResultVo<String> resultVo = new ResultVo<>();
                resultVo.resultFail("失败");
                logger.error("HystrixClientFallback.readPlateNum terminalType:" + terminalType + ";terminalId:" + terminalId);
                return resultVo;
            }
        };
    }
}
