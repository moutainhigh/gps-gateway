package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.OutGpsBusinessDto;

/**
 * author : cyc
 * Date : 2019-07-05
 */
public interface OutGpsService {

    /**
     * 设置电子运单状态并且推送运单状态变更事件
     *
     * @param outGpsBusinessDto
     */
    void setBusinessStatus(OutGpsBusinessDto outGpsBusinessDto) throws Exception;


}
