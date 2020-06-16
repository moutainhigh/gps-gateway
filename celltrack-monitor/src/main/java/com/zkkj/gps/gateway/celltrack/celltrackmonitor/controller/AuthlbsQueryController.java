package com.zkkj.gps.gateway.celltrack.celltrackmonitor.controller;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.ResultVo;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.authlbsquery.AuthlbsQueryBean;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.service.AuthlbsLocalService;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.utils.LoggerUtils;
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
@Api(value = "AuthlbsQueryController", description = "实时定位接口(返回经纬度)Api接口")
@Slf4j
public class AuthlbsQueryController {

    @Autowired
    private AuthlbsLocalService authlbsLocalService;

    @ApiOperation(value = "测试Api！", notes = "测试接口！")
    @PostMapping(value = "/authlbsqueryhello")
    public ResultVo<String> getHelloWorld(String arg) {
        ResultVo resultVo = new ResultVo();
        resultVo.resultSuccess("Hello World!" + arg,"成功！");
        return resultVo;
    }

    @ApiOperation(value = "实时定位")
    @GetMapping("/query")
    public ResultVo<AuthlbsQueryBean> authlbsQuery(@RequestParam(value = "mobile") @ApiParam(value = "需要开通定位的手机号码（11位长度）", required = true) String mobile) {
        ResultVo<AuthlbsQueryBean> authlbsQuery = authlbsLocalService.authlbsquery(mobile);
        LoggerUtils.info(log,mobile,"实时定位结果：【" + authlbsQuery.toString() + "】");
        return authlbsQuery;
    }

}
