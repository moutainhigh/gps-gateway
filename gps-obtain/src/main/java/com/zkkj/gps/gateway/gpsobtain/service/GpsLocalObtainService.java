package com.zkkj.gps.gateway.gpsobtain.service;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.gpsobtain.GpsObtainBean;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;

/**
 * 获取定位信息本地业务
 * @author suibozhuliu
 */
public interface GpsLocalObtainService {

    /**
     * 通过手机号、车牌号获取定位信息
     * @param gpsObtainBean
     * @return
     */
    ResultVo<PointBaseBean> getGpsInfo(GpsObtainBean gpsObtainBean);

}
