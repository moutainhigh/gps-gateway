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
 * 开通回调Api接口（供神州基站平台调用）
 */
@RestController
@RequestMapping(value = "/authlbs")
@CrossOrigin
@Api(value = "AuthlbsCallBackController", description = "开通回调Api接口（供神州基站平台调用）")
@Slf4j
public class AuthlbsCallBackController {

    @Autowired
    private AuthlbsLocalService authlbsLocalService;

    @ApiOperation(value = "测试Api！", notes = "测试接口！")
    @PostMapping(value = "/authlbscallbackhello")
    public ResultVo<String> getHelloWorld(String arg) {
        ResultVo resultVo = new ResultVo();
        resultVo.resultSuccess("Hello World!" + arg,"成功！");
        return resultVo;
    }

    @ApiOperation(value = "开通回调")
    @GetMapping("/callback")
    public void callback(@RequestParam(value = "mobileno") @ApiParam(value = "手机号", required = true) String mobileno,
                                            @RequestParam(value = "action") @ApiParam(value = "操作详情", required = true) String action,
                                            @RequestParam(value = "sign") @ApiParam(value = "md5(手机号+密钥)就是 md5(mobile+secret)的值", required = true) String sign,
                                            @RequestParam(value = "msg") @ApiParam(value = "action对应的消息", required = true) String msg) {
        authlbsLocalService.callBack(mobileno,action,sign,msg);
    }

}
