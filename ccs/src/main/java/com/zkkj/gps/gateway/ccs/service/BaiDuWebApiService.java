package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.baiDuDto.BaiDuResult;
import com.zkkj.gps.gateway.ccs.dto.baiDuDto.Result;
import com.zkkj.gps.gateway.ccs.dto.baiDuDto.ReverseResult;
import feign.Param;
import feign.RequestLine;

/**
 * author : cyc
 * Date : 2019/11/13
 */
public interface BaiDuWebApiService {


    /**
     * 百度地理编码
     *
     * @param address
     * @param output
     * @param ak
     * @return
     */
    @RequestLine("GET /geocoding/v3/?address={address}&output={output}&ak={ak}")
    BaiDuResult<Result> v3(@Param("address") String address, @Param("output") String output, @Param("ak") String ak);

    /**
     * 百度逆地理编码
     *
     * @param ak
     * @param output
     * @param coordType
     * @param location
     * @return
     */
    @RequestLine("GET /reverse_geocoding/v3/?ak={ak}&output={output}&coordtype={coordType}&location={location}")
    BaiDuResult<ReverseResult> reverseGeocoding(@Param("ak")String ak, @Param("output")String output, @Param("coordType")String coordType, @Param("location")String location);
}
