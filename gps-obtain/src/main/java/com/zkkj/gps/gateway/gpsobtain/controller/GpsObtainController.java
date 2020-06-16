//package com.zkkj.gps.gateway.gpsobtain.controller;
//
//import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
//import com.zkkj.gps.gateway.gpsobtain.entity.gpsobtain.GpsObtainBean;
//import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
//import com.zkkj.gps.gateway.gpsobtain.service.GpsLocalObtainService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Gps定位查询/获取
// * @author suibozhuliu
// */
//@RestController
//@RequestMapping(value = "/gpsobtain")
//@CrossOrigin
//@Api(value = "GpsObtainController", description = "Gps定位查询/获取Api接口")
//@Slf4j
//public class GpsObtainController {
//
//    @Autowired
//    private GpsLocalObtainService gpsService;
//
//    @ApiOperation(value = "测试Api！", notes = "测试接口！")
//    @PostMapping(value = "/hello")
//    public ResultVo<String> getHelloWorld(String arg) {
//        ResultVo resultVo = new ResultVo();
//        resultVo.resultSuccess("Hello World!" + arg,"成功！");
//        return resultVo;
//    }
//
//    @ApiOperation(value = "获取定位信息")
//    @PostMapping("/getGpsInfo")
//    public ResultVo<PointBaseBean> getGpsInfo(@RequestBody @ApiParam(value = "定位信息获取模型", required = true) GpsObtainBean gpsObtainBean) {
//        ResultVo<PointBaseBean> resultVo = gpsService.getGpsInfo(gpsObtainBean);
//        log.info("定位信息获取结果：【" + resultVo.toString() + "");
//        return resultVo;
//    }
//
//}
