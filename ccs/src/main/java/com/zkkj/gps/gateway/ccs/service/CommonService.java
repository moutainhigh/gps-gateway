package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.token.PlatformLogin;
import com.zkkj.gps.gateway.ccs.dto.token.PlatformLoginTruckNum;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CommonService {


    /*
     * @Author lx
     * @Description 请求公共管理平台登录接口
     * @Date 16:17 2019/7/14
     * @Param
     * @return
     **/
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST platformLogin/login")
    ResultVo<List<TruckAndTerminal>> Login(@RequestBody PlatformLogin platformLogin);

    /*
     * @Author lx
     * @Description 请求公共管理平台登录接口(车牌号)
     * @Date 16:17 2019/7/14
     * @Param
     * @return
     **/
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST platformLogin/loginByTruckNum")
    ResultVo<List<TruckAndTerminal>> loginByTruckNum(@RequestBody PlatformLoginTruckNum platformLogin);

    /*@Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("GET platformLogin/login?appkey={appkey}&appsecret={appsecret}&terminals={terminals}")
    ResultVo<List<TruckAndTerminal>> Login(@Param("appkey") String appkey, @Param("appsecret") String appsecret, @Param("terminals") String terminals);
*/
//    /*
//     * @Author lx
//     * @Description 根据token获取平台登录信息
//     * @Date 16:31 2019/7/14
//     * @Param
//     * @return
//     **/
//    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
//    @RequestLine("GET platformLogin/getTerminalInfoByToken?token={token}")
//    ResultVo<TokenUser> getTerminalInfoByToken(@Param("token") String token);

    /*
     * @Author zkkj
     * @Description 请求公共管理平台登录接口(车牌号，平台3.0地图通用接口)
     * @Date 16:17 2020/06/03
     * @Param
     * @return
     **/
    @Headers({"Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"})
    @RequestLine("POST platformLogin/loginByTruckNumAndKey")
    ResultVo<List<TruckAndTerminal>> loginByTruckNumAndKey(@RequestBody PlatformLoginTruckNum platformLogin);

}
