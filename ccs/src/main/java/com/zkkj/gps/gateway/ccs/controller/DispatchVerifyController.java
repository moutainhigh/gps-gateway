package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.entity.dispatch.dispatchverity.VehicleAreaBean;
import com.zkkj.gps.gateway.ccs.entity.dispatch.dispatchverity.VehicleBaseBean;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.DispatchVerifyService;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * 车辆派车信息验证相关
 * @author suibozhuliu
 */
@Slf4j
@RestController
@RequestMapping(value = "/dispatchVerify")
@CrossOrigin
@Api(value = "车辆派车信息验证相关", description = "车辆派车信息验证相关api")
public class DispatchVerifyController {

    @Autowired
    private CommonBaseUtil commonBaseUtil;

    @Autowired
    private DispatchVerifyService dispatchVerifyService;

    @Autowired
    private LocationWebApiService locationWebApiService;

    @Value("${out.line.time}")
    private int outLineTime;

    @Autowired
    private IGenerator iGenerator;

    /*@ApiOperation(value = "车辆派车信息验证接口", notes = "车辆派车信息验证api")
    @PostMapping("/vehicleDispatchVerify")
    public ResultVo<String> vehicleDispatchVerify(@RequestBody VehicleDispatchBean vehicleDispatchBean) {
        ResultVo<String> resultVo = new ResultVo<>();
        if (ObjectUtils.isEmpty(vehicleDispatchBean)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        if (StringUtils.isEmpty(vehicleDispatchBean.getLicensePlate())){
            resultVo.resultFail("车牌号不能为空！");
            return resultVo;
        }
        try {
            String terminalNo = null;
            String licensePlate = vehicleDispatchBean.getLicensePlate();
            List<TruckAndTerminal> truckAndTerminals = commonBaseUtil.getTruckAndTerminal();
            if (!CollectionUtils.isEmpty(truckAndTerminals)) {//所属用户的车辆及终端不为空
                for (TruckAndTerminal truckAndTerminal : truckAndTerminals) {
                    if (!ObjectUtils.isEmpty(truckAndTerminal) &&
                            !StringUtils.isEmpty(truckAndTerminal.getTruckNo()) &&
                            truckAndTerminal.getTruckNo().equals(licensePlate)) {
                        terminalNo = truckAndTerminal.getTerminalNo();
                        break;
                    }
                }
                if (StringUtils.isEmpty(terminalNo)){
                    resultVo.resultFail("未查询到车辆所对应的GPS设备！");
                    return resultVo;
                }
                if (vehicleDispatchBean.isOnlineGps()){//要求在线
                    if (dispatchVerifyService.terminalOnLineVerify(terminalNo)) {//在线
                        if (vehicleDispatchBean.isGetOrderLocationRequire()) {//有位置要求
                            if (!ObjectUtils.isEmpty(vehicleDispatchBean.getAreaInfo())) {//区域不为空
                                if (dispatchVerifyService.terminalInAreaVerify(terminalNo, vehicleDispatchBean.getAreaInfo())) {//在区域
                                    resultVo.resultSuccess("车辆在线且在指定区域！");
                                    resultVo.setMsg("车辆在线且在指定区域！");
                                } else {//不在区域
                                    resultVo.resultFail("车辆在线但不在指定区域！");
                                }
                            } else {//区域为空
                                resultVo.resultFail("对车辆有位置要求，但区域信息为空！");
                            }
                        } else {//无位置要求
                            resultVo.resultFail("车辆无位置要求！");
                        }
                    } else {//不在线
                        resultVo.resultFail("车辆不在线！");
                    }
                } else {//不要求在线
                    resultVo.resultFail("车辆不要求在线！");
                }
            } else {//所属用户的车辆及终端为空
                log.error("车辆派车、接单信息验证失败，当前用户下的车辆终端列表为空！");
            }
        } catch (ParamException px){
            resultVo.resultFail("验证失败，" + px.getMessage());
            log.error("DispatchVerifyController.vehicleDispatchVerify is ParamException error", px);
        } catch (Exception e){
            resultVo.resultFail("验证失败，请重试！");
            log.error("DispatchVerifyController.vehicleDispatchVerify is error", e);
        }
        return resultVo;
    }*/

    @ApiOperation(value = "车辆是否在指定区域验证接口", notes = "车辆是否在指定区域验证Api")
    @PostMapping("/vehicleAreaVerify")
    public ResultVo<String> vehicleAreaVerify(@RequestBody VehicleAreaBean vehicleAreaBean) {
        ResultVo<String> resultVo = new ResultVo<>();
        if (ObjectUtils.isEmpty(vehicleAreaBean)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        if (StringUtils.isEmpty(vehicleAreaBean.getLicensePlate())){
            resultVo.resultFail("车牌号不能为空！");
            return resultVo;
        }
        if (StringUtils.isEmpty(vehicleAreaBean.getAreaInfo())){
            resultVo.resultFail("区域不能为空！");
            return resultVo;
        }
        try {
            String terminalNo = null;
            String licensePlate = vehicleAreaBean.getLicensePlate();
            List<TruckAndTerminal> truckAndTerminals = commonBaseUtil.getTruckAndTerminal();
            if (!CollectionUtils.isEmpty(truckAndTerminals)) {//所属用户的车辆及终端不为空
                for (TruckAndTerminal truckAndTerminal : truckAndTerminals) {
                    if (!ObjectUtils.isEmpty(truckAndTerminal) &&
                            !StringUtils.isEmpty(truckAndTerminal.getTruckNo()) &&
                            truckAndTerminal.getTruckNo().equals(licensePlate)) {
                        terminalNo = truckAndTerminal.getTerminalNo();
                        break;
                    }
                }
                if (StringUtils.isEmpty(terminalNo)) {//未查询到车辆所对应的GPS设备
                    terminalNo = licensePlate;//将车牌号作为设备号
                }
                try {
                    if (dispatchVerifyService.terminalInAreaVerify(terminalNo, vehicleAreaBean.getAreaInfo())) {//在区域
                        resultVo.resultSuccess("车辆在指定区域！");
                        resultVo.setMsg("车辆在指定区域！");
                        return resultVo;
                    }
                } catch (Exception px){
                    log.error("缓存中查找当前车辆定位信息异常：", px);
                }
                if (vehicleAreaBean.getReqZjxlFlag() == 1){//需要从中交兴路查询
                    ResultVo<RealBaseGpsPositionInfo> realPositionZjxl = locationWebApiService.getGpsByLicensePlate(licensePlate);
                    if (!ObjectUtils.isEmpty(realPositionZjxl) &&
                            realPositionZjxl.isSuccess() &&
                            !ObjectUtils.isEmpty(realPositionZjxl.getData())) {
                        RealBaseGpsPositionInfo zjxlData = realPositionZjxl.getData();
                        if (!ObjectUtils.isEmpty(zjxlData) && !ObjectUtils.isEmpty(zjxlData.getLatitude()) && !ObjectUtils.isEmpty(zjxlData.getLongitude())){
                            InAreaDto areaInfo = vehicleAreaBean.getAreaInfo();
                            AreaDto area = iGenerator.convert(areaInfo, AreaDto.class);
                            boolean isInAreaFlag = GPSPositionUtil.checkSingleInArea(new Point2D.Double(zjxlData.getLongitude(),
                                    zjxlData.getLatitude()), area);
                            if (isInAreaFlag){//在指定区域
                                resultVo.resultSuccess("车辆在指定区域！");
                                resultVo.setMsg("车辆在指定区域！");
                                return resultVo;
                            }
                        }
                    }
                }
                resultVo.resultFail("车辆不在指定区域！");
            } else {
                resultVo.resultFail("该用户下未找到当前车辆！");
            }
        } catch (Exception e){
            resultVo.resultFail("验证失败，请重试！");
            log.error("DispatchVerifyController.vehicleAreaVerify is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "车辆是否在线验证接口", notes = "车辆是否在线验证Api")
    @PostMapping("/vehicleOnlineVerify")
    public ResultVo<String> vehicleOnlineVerify(@RequestBody VehicleBaseBean vehicleBaseBean) {
        ResultVo<String> resultVo = new ResultVo<>();
        if (ObjectUtils.isEmpty(vehicleBaseBean)){
            resultVo.resultFail("参数有误，请检查！");
            return resultVo;
        }
        if (StringUtils.isEmpty(vehicleBaseBean.getLicensePlate())){
            resultVo.resultFail("车牌号不能为空！");
            return resultVo;
        }
        try {
            String terminalNo = null;
            String licensePlate = vehicleBaseBean.getLicensePlate();
            List<TruckAndTerminal> truckAndTerminals = commonBaseUtil.getTruckAndTerminal();
            if (!CollectionUtils.isEmpty(truckAndTerminals)) {//所属用户的车辆及终端不为空
                for (TruckAndTerminal truckAndTerminal : truckAndTerminals) {
                    if (!ObjectUtils.isEmpty(truckAndTerminal) &&
                            !StringUtils.isEmpty(truckAndTerminal.getTruckNo()) &&
                            truckAndTerminal.getTruckNo().equals(licensePlate)) {
                        terminalNo = truckAndTerminal.getTerminalNo();
                        break;
                    }
                }
                if (StringUtils.isEmpty(terminalNo)) {//未查询到车辆所对应的GPS设备
                    terminalNo = licensePlate;//将车牌号作为设备号
                }
                try {
                    if (dispatchVerifyService.terminalOnLineVerify(terminalNo)){
                        resultVo.resultSuccess("车辆在线！");
                        resultVo.setMsg("车辆在线！");
                        return resultVo;
                    }
                } catch (Exception e){
                    log.error("缓存中查找当前车辆定位信息异常：", e);
                }
                if (vehicleBaseBean.getReqZjxlFlag() == 1){//需要从中交兴路查询
                    ResultVo<RealBaseGpsPositionInfo> realPositionZjxl = locationWebApiService.getGpsByLicensePlate(licensePlate);
                    if (!ObjectUtils.isEmpty(realPositionZjxl) &&
                            realPositionZjxl.isSuccess() &&
                            !ObjectUtils.isEmpty(realPositionZjxl.getData())) {
                        RealBaseGpsPositionInfo zjxlData = realPositionZjxl.getData();
                        if (!ObjectUtils.isEmpty(zjxlData) && !ObjectUtils.isEmpty(zjxlData.getGpsTime())){
                            if (DateTimeUtils.durationMinutes(zjxlData.getGpsTime(), DateTimeUtils.getCurrentLocalDateTime()) <= outLineTime){
                                resultVo.resultSuccess("车辆在线！");
                                resultVo.setMsg("车辆在线！");
                                return resultVo;
                            }
                        }
                    }
                }
                resultVo.resultFail("车辆不在线！");
            } else {
                resultVo.resultFail("该用户下未找到当前车辆！");
            }
        } catch (ParamException px){
            resultVo.resultFail("验证失败，" + px.getMessage());
            log.error("DispatchVerifyController.vehicleOnlineVerify is ParamException error", px);
        } catch (Exception e){
            resultVo.resultFail("验证失败，请重试！");
            log.error("DispatchVerifyController.vehicleOnlineVerify is error", e);
        }
        return resultVo;
    }

}
