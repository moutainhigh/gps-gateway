package com.zkkj.gps.gateway.gpsobtain.controller;

import com.zkkj.gps.gateway.gpsobtain.entity.ResultVo;
import com.zkkj.gps.gateway.gpsobtain.entity.position.PointBaseBean;
import com.zkkj.gps.gateway.gpsobtain.entity.zjxlbean.truckbean.TrackBaseBean;
import com.zkkj.gps.gateway.gpsobtain.service.ZjxlGpsObtainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/zjxlgps")
@CrossOrigin
@Api(value = "ZjxlController", description = "中交兴路定位Api接口")
@Slf4j
public class ZjxlController {

    @Autowired
    private ZjxlGpsObtainService zjxlGpsObtainService;

    @ApiOperation(value = "通过单个车牌获取实时定位")
    @PostMapping("/getGpsByLicensePlate")
    public ResultVo<PointBaseBean> getGpsByLicensePlate(@RequestParam(value = "licensePlate") @ApiParam(value = "车牌号", required = true) String licensePlate) {
        ResultVo<PointBaseBean> resultVo = zjxlGpsObtainService.getGpsByLicensePlate(licensePlate);
        log.info("中交兴路定位信息获取结果：【" + resultVo.toString() + "");
        return resultVo;
    }

    @ApiOperation(value = "通过车牌批量获取实时定位")
    @PostMapping("/getGpsByLicensePlates")
    public ResultVo<List<PointBaseBean>> getGpsByLicensePlates(@RequestParam(value = "licensePlates") @ApiParam(value = "车牌号（多个车牌需用,隔开）", required = true) String licensePlates) {
        ResultVo<List<PointBaseBean>> resultVo = zjxlGpsObtainService.getGpsByLicensePlates(licensePlates);
        log.info("中交兴路通过车牌批量获取定位信息结果：【" + resultVo.toString() + "");
        return resultVo;
    }

    @ApiOperation(value = "通过单个车牌获取车辆轨迹")
    @PostMapping("/getTruckByLicensePlate")
    public ResultVo<TrackBaseBean> getTruckByLicensePlate(@RequestParam(value = "licensePlate") @ApiParam(value = "车牌号", required = true) String licensePlates,
                                                          @RequestParam(value = "startTime") @ApiParam(value = "查询开始时间（注：距结束时间小于24h，格式：yyyy-MM-dd HH:mm:ss）", required = true) String startTime,
                                                          @RequestParam(value = "endTime") @ApiParam(value = "查询结束时间（注：距开始时间小于24h，格式：yyyy-MM-dd HH:mm:ss）", required = true) String endTime) {
        ResultVo<TrackBaseBean> resultVo = zjxlGpsObtainService.getTruckByLicensePlate(licensePlates,startTime,endTime);
        log.info("中交兴路通过车牌批量获取定位信息结果：【" + resultVo.toString() + "");
        return resultVo;
    }




}
