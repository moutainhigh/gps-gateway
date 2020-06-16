package com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean;

import lombok.Data;
import lombok.ToString;

/**
 * 批量查询当前定位信息响应模型
 * @author suibozhuliu
 */
@Data
@ToString(callSuper = true)
public class ZjxlBatchPointBean extends ZjxlPointBean {

    /**
     * 车牌号
     */
    private String vno;

    /**
     * 响应状态
     */
    private int state;

    /**
     * 车辆总数
     */
    private String vco;

}
