package com.zkkj.gps.gateway.gpsZjxl.fallback;

import com.zkkj.gps.gateway.gpsZjxl.common.ResultVo;
import com.zkkj.gps.gateway.gpsZjxl.entity.position.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.gpsZjxl.service.LocationWebApiService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author : cyc
 * Date : 2019/12/20
 */
@Component
public class HystrixClientFallback implements FallbackFactory<LocationWebApiService> {

    private Logger logger = LoggerFactory.getLogger(HystrixClientFallback.class);

    @Override
    public LocationWebApiService create(Throwable throwable) {
        logger.error("HystrixClientFallback.throwable is error", throwable);
        return licensePlates -> {
            logger.error("HystrixClientFallback.getGpsByLicensePlates is error", "系统异常");
            ResultVo<List<RealBaseGpsPositionInfo>> resultVo = new ResultVo<>();
            resultVo.resultFail("系统异常");
            return resultVo;
        };
    }
}
