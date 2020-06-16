package com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.basebean;

import lombok.Data;

/**
 * 神州基站定位响应基类模型
 * @author suibozhuliu
 */
@Data
public class AuthlbsBaseBean {

    /**
     * 请求返回结果状态码
     */
    private int resid;

    /**
     * 请求返回结果提示
     */
    private String resmsg;

}
