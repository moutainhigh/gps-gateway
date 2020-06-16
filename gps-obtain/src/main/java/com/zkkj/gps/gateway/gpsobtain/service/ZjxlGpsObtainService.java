package com.zkkj.gps.gateway.gpsobtain.service;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.truckbean.TrackBaseBean;

import java.util.List;

/**
 * 中交兴路平台获取定位数据业务
 * @author suibozhuliu
 */
public interface ZjxlGpsObtainService {

    /**
     * 通过车牌号在中交兴路平台查询当前实时位置
     * @param licensePlate
     * @return
     */
    ResultVo<PointBaseBean> getGpsByLicensePlate (String licensePlate);

    /**
     * 通过车牌号查询实时位置（多车）
     * @param licensePlates
     * @return
     */
    ResultVo<List<PointBaseBean>> getGpsByLicensePlates(String licensePlates);

    /**
     * 通过车牌号查询车辆轨迹
     * @param licensePlate
     * @param startTime
     * @param endTime
     * @return
     */
    ResultVo<TrackBaseBean> getTruckByLicensePlate (String licensePlate, String startTime, String endTime);

}
