package com.zkkj.gps.gateway.gpsobtain.service;

import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsclose.AuthlbsCloseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsopen.AuthlbsOpenBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsquery.AuthlbsQueryBean;
import com.zkkj.gps.gateway.gpsobtain.entity.authlbs.authlbsstatus.AuthlbsStatusBean;
import feign.Param;
import feign.RequestLine;

/**
 * 神州手机基站定位服务
 * @author suibozhuliu
 */
public interface AuthlbsRemoteService {

    /**
     * 开通授权定位API接口
     * @param key
     * @param secret
     * @param mobile
     * @return
     */
    @RequestLine("GET /authlbsopen/?key={key}&secret={secret}&mobile={mobile}")
    AuthlbsOpenBean authlbsopen(@Param("key") String key, @Param("secret") String secret, @Param("mobile") String mobile) throws Exception;

    /**
     * 状态查询接口
     * @param key
     * @param secret
     * @param mobile
     * @return
     */
    @RequestLine("GET /authlbsstatus/?key={key}&secret={secret}&mobile={mobile}")
    AuthlbsStatusBean authlbsstatus(@Param("key") String key, @Param("secret") String secret, @Param("mobile") String mobile) throws Exception;

    /**
     * 实时定位接口(返回经纬度)
     * @param key
     * @param secret
     * @param mobile
     * @return
     */
    @RequestLine("GET /authlbsquery/?key={key}&secret={secret}&mobile={mobile}")
    AuthlbsQueryBean authlbsquery(@Param("key") String key, @Param("secret") String secret, @Param("mobile") String mobile) throws Exception;

    /**
     * 关闭定位接口
     * @param key
     * @param secret
     * @param mobile
     * @return
     */
    @RequestLine("GET /authlbsclose/?key={key}&secret={secret}&mobile={mobile}")
    AuthlbsCloseBean authlbsclose(@Param("key") String key, @Param("secret") String secret, @Param("mobile") String mobile) throws Exception;

}
