package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealBaseGpsPositionInfo;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * author : cyc
 * Date : 2019/11/14
 */
public interface LocationWebApiService {

    /**
     * 获取根据手机号定位信息
     *
     * @param phoneNum
     * @return
     */
    @RequestLine("GET /authlbs/getGpsByphoneNum?phoneNum={phoneNum}")
    ResultVo<RealBaseGpsPositionInfo> getGpsByphoneNum(@Param("phoneNum") String phoneNum);


    /**
     * 获取根据车牌号定位信息
     *
     * @param licensePlate
     * @return
     */
    @RequestLine("POST /zjxlgps/getGpsByLicensePlate?licensePlate={licensePlate}")
    ResultVo<RealBaseGpsPositionInfo> getGpsByLicensePlate(@Param("licensePlate") String licensePlate);

    /**
     * 通过车牌批量获取实时定位
     *
     * @param licensePlates
     * @return
     */
    @RequestLine("POST /zjxlgps/getGpsByLicensePlates?licensePlates={licensePlates}")
    ResultVo<List<RealBaseGpsPositionInfo>> getGpsByLicensePlates(@Param("licensePlates") String licensePlates);

    /**
     * 通过手机号发送定位短信
     *
     * @param phoneNum
     * @return
     */
    @RequestLine("GET /authlbs/sendMsgToAuthlbs?phoneNum={phoneNum}")
    ResultVo<Boolean> sendMsgToAuthlbs(@Param("phoneNum") String phoneNum);

    /**
     * 通过车牌号从中交线路获取历史轨迹信息
     *
     * @param licensePlate
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestLine("POST /zjxlgps/getTruckByLicensePlate?licensePlate={licensePlate}&startTime={startTime}&endTime={endTime}")
    ResultVo<HisGpsPositionInfo> getTruckByLicensePlate(@Param("licensePlate") String licensePlate, @Param("startTime") String startTime, @Param("endTime") String endTime);

}
