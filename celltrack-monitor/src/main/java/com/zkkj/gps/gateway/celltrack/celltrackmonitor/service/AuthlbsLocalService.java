package com.zkkj.gps.gateway.celltrack.celltrackmonitor.service;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.ResultVo;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.authlbsquery.AuthlbsQueryBean;
import feign.Param;
import feign.RequestLine;

/**
 * 处理远程接口调用的本地业务服务
 */
public interface AuthlbsLocalService {

    /**
     * 开通授权定位API接口
     * @param mobile
     * @return
     */
    ResultVo<String> authlbsopen(@Param("mobile") String mobile);

    /**
     * 状态查询接口
     * @param mobile
     * @return
     */
    ResultVo<String> authlbsstatus(@Param("mobile") String mobile);

    /**
     * 实时定位接口(返回经纬度)
     * @param mobile
     * @return
     */
    ResultVo<AuthlbsQueryBean> authlbsquery(@Param("mobile") String mobile);

    /**
     * 关闭定位接口
     * @param mobile
     * @return
     */
    ResultVo<String> authlbsclose(@Param("mobile") String mobile);

    /**
     * 开通回调接口
     * @param mobileno
     * @param action
     * @param sign
     * @param msg
     */
    void callBack(String mobileno, String action, String sign,String msg);

}
