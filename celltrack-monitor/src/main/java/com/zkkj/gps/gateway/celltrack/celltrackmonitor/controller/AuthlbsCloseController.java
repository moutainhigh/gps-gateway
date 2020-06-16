package com.zkkj.gps.gateway.celltrack.celltrackmonitor.controller;

import com.zkkj.gps.gateway.celltrack.celltrackmonitor.entity.ResultVo;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.service.AuthlbsLocalService;
import com.zkkj.gps.gateway.celltrack.celltrackmonitor.utils.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 定位关闭Api接口
 */
@RestController
@RequestMapping(value = "/authlbs")
@CrossOrigin
@Api(value = "AuthlbsCloseController", description = "定位关闭Api接口")
@Slf4j
public class AuthlbsCloseController {

    @Autowired
    private AuthlbsLocalService authlbsLocalService;

    @ApiOperation(value = "测试Api！", notes = "测试接口！")
    @PostMapping(value = "/authlbsclosehello")
    public ResultVo<String> getHelloWorld(String arg) {
        ResultVo resultVo = new ResultVo();
        resultVo.resultSuccess("Hello World!" + arg,"成功！");
        return resultVo;
    }

    @ApiOperation(value = "定位关闭")
    @GetMapping("/close")
    public ResultVo<String> close (@RequestParam(value = "mobile") @ApiParam(value = "需要开通定位的手机号码（11位长度）", required = true) String mobile) {
        ResultVo<String> authlbsClose = authlbsLocalService.authlbsclose(mobile);
        LoggerUtils.info(log,mobile,"定位关闭结果：【" + authlbsClose.toString() + "】");
        return authlbsClose;
    }

}
