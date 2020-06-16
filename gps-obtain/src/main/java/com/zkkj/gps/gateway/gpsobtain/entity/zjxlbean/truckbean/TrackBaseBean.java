package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.truckbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 轨迹基础模型
 */
@Data
public class TrackBaseBean {

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号", name = "licensePlate")
    private String licensePlate;

    /**
     * 坐标点集合
     */
    @ApiModelProperty(value = "坐标点集合", name = "pointList")
    private List<TrackPointBean> pointList;

}
