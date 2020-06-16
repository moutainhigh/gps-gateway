package com.zkkj.gps.gateway.gpsobtain.controller;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.service.AuthlbsLocalService;
import com.zkkj.gps.gateway.gpsobtain.utils.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实时定位接口(返回经纬度)Api接口
 */
@RestController
@RequestMapping(value = "/authlbs")
@CrossOrigin
@Api(value = "AuthlbsController", description = "基站定位Api接口")
@Slf4j
public class AuthlbsController {

    @Autowired
    private AuthlbsLocalService authlbsLocalService;

    /*@ApiOperation(value = "实时定位")
    @GetMapping("/query")
    public ResultVo<AuthlbsQueryBean> authlbsQuery(@RequestParam(value = "mobile") @ApiParam(value = "需要开通定位的手机号码（11位长度）", required = true) String mobile) {
        ResultVo<AuthlbsQueryBean> authlbsQuery = authlbsLocalService.authlbsquery(mobile);
        LoggerUtils.info(log,mobile,"实时定位结果：【" + authlbsQuery.toString() + "】");
        return authlbsQuery;
    }*/

    @ApiOperation(value = "通过手机号实时定位")
    @GetMapping("/getGpsByphoneNum")
    public ResultVo<PointBaseBean> getGpsByphoneNum(@RequestParam(value = "phoneNum") @ApiParam(value = "需要定位的手机号码（11位长度）", required = true) String phoneNum) {
        ResultVo<PointBaseBean> authlbsQuery = authlbsLocalService.getGpsByphoneNum(phoneNum);
        LoggerUtils.info(log,phoneNum,"实时定位结果：【" + authlbsQuery.toString() + "】");
        return authlbsQuery;
    }

    @ApiOperation(value = "通过手机号发送定位短信")
    @GetMapping("/sendMsgToAuthlbs")
    public ResultVo<Boolean> sendMsgToAuthlbs(@RequestParam(value = "phoneNum") @ApiParam(value = "需要定位的手机号码（11位长度）", required = true) String phoneNum) {
        ResultVo<Boolean> authlbsQuery = authlbsLocalService.sendMsgToAuthlbs(phoneNum);
        LoggerUtils.info(log,phoneNum,"发送短信结果：【" + authlbsQuery.toString() + "】");
        return authlbsQuery;
    }

}
