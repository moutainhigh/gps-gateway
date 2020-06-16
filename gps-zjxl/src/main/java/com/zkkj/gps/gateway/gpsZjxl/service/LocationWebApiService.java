package com.zkkj.gps.gateway.gpsZjxl.service;

import com.zkkj.gps.gateway.gpsZjxl.common.ResultVo;
import com.zkkj.gps.gateway.gpsZjxl.entity.position.RealBaseGpsPositionInfo;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * author : cyc
 * Date : 2019/11/14
 */
public interface LocationWebApiService {

    /**
     * 通过车牌批量获取实时定位
     *
     * @param licensePlates
     * @return
     */
    @RequestLine("POST /zjxlgps/getGpsByLicensePlates?licensePlates={licensePlates}")
    ResultVo<List<RealBaseGpsPositionInfo>> getGpsByLicensePlates(@Param("licensePlates") String licensePlates);


}
