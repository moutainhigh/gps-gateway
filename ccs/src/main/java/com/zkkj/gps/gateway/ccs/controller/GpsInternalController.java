package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.*;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gps/internal")
@CrossOrigin
@Api(value = "点位信息接口", description = "点位信息接口api")
public class GpsInternalController {

    private Logger logger = LoggerFactory.getLogger(GpsInternalController.class);

    @Value("${out.line.time}")
    private int outLineTime;

    @Autowired
    private IGenerator iGenerator;

    @Autowired
    private GpsInternalService gpsInternalService;

    @PostMapping(value = "positionChange")
    @ApiOperation(value = "根据appkey更新终端的定位信息", notes = "根据appkey更新终端的定位信息api")
    public ResultVo<Boolean> positionChange(@RequestBody BaseGPSPositionDto baseGPSPositionDto) {
        ResultVo<Boolean> result = new ResultVo<>();
        try {
            //BeanValidate.checkParam(udPGPSPositionDTO);
            gpsInternalService.positionChange(baseGPSPositionDto.getTerminalId(), baseGPSPositionDto, null);
            result.setMsg("终端的点位信息更新成功");
        } catch (Exception e) {
            logger.error("GpsInternalController.positionChange is error", e);
            result.resultFail("系统异常:更新定位信息失败");
        }
        return result;
    }


    @PostMapping("judgeTerminalInArea")
    @ApiOperation(value = "判断终端是否在区域内接口", notes = "判断终端是否在区域内接口api")
    public ResultVo<Integer> judgeTerminalInArea(@RequestBody TerminalAreaDto terminalAreaDto) {
        ResultVo<Integer> result = new ResultVo<>();
        try {
            BeanValidate.checkParam(terminalAreaDto);   //进行对象数据校验
            //获取终端点位信息
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalAreaDto.getTerminalId());
            if (ObjectUtils.isEmpty(baseGPSPositionDto) || ObjectUtils.isEmpty(baseGPSPositionDto.getPoint())) {    //判断该终端的设备号是否存在
                result.resultFail("没有该终端设备对象的点位信息,请核对设备号");
                result.setData(3);
                return result;
            }
            BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();  //获取终端点位数据
            //判断该终端设备是否在线 true：掉线   false：不掉线
            boolean dateFlag = DateTimeUtils.durationMinutes(basicPositionDto.getDate(), DateTimeUtils.getCurrentLocalDateTime()) > outLineTime;
            if (dateFlag) {
                result.resultFail("该终端设备已掉线");
                result.setData(0);
                return result;
            }

            if (!this.gpsInternalService.getIsInAreaFlag(terminalAreaDto,basicPositionDto)) {
                result.resultFail("该终端设备在线但不在指定区域");
                result.setData(1);
                return result;
            }
            result.setData(2);
            result.setMsg("该终端设备在线且在指定区域");
            return result;
        } catch (ParamException px) {
            result.resultFail("系统异常:" + px.getMessage());
            logger.error("GpsInternalController.judgeTerminalInArea is error", px);
        } catch (Exception e) {
            logger.error("GpsInternalController.judgeTerminalInArea is error", e);
            result.resultFail("系统异常:验证设备是否在区域内失败");
        }
        return result;
    }


    @PostMapping("judgeTerminalInAreaList")
    @ApiOperation(value = "判断多个终端是否在区域内接口", notes = "判断多个终端是否在区域内接口api")
    public ResultVo<Map<Integer,List<String>>> judgeTerminalInAreaList(@RequestBody TerminalAreaDto terminalAreaDto) {
        ResultVo<Map<Integer,List<String>>> result = new ResultVo<>();
        try {
            Map<Integer, List<String>> integerListMap = this.gpsInternalService.judgeTerminalInAreaList(terminalAreaDto);
            result.setData(integerListMap);
            return result;
        } catch (ParamException px) {
            result.resultFail("系统异常:" + px.getMessage());
            logger.error("GpsInternalController.judgeTerminalInArea is error", px);
        } catch (Exception e) {
            logger.error("GpsInternalController.judgeTerminalInArea is error", e);
            result.resultFail("系统异常:验证设备是否在区域内失败");
        }

        return result;
    }



    @PostMapping("getAllCarsInArea")
    @ApiOperation(value = "获取区域内所有车辆接口", notes = "获取区域内所有车辆接口api")
    public ResultVo<List<String>> getAllCarsInArea(@RequestBody InAreaDto areaDto) {
        ResultVo<List<String>> result = new ResultVo<>();
        try {
            BeanValidate.checkParam(areaDto);
            if (validateParams(areaDto, result)) return result;
            List<String> list = gpsInternalService.getAllCarsInArea(areaDto, outLineTime);
            result.resultSuccess(list);
        } catch (ParamException px) {
            logger.error("GpsInternalController.getAllCarsInArea is error", px);
            result.resultFail("系统异常:" + px.getMessage());
        } catch (Exception e) {
            logger.error("GpsInternalController.getAllCarsInArea is error", e);
            result.resultFail("系统异常:获取区域内所有车辆失败");
        }
        return result;
    }

    /**
     * 校验参数
     *
     * @param areaDto
     * @param result
     * @return
     */
    private boolean validateParams(@RequestBody InAreaDto areaDto, ResultVo<List<String>> result) {
        if (GraphTypeEnum.CIRCLE.equals(areaDto.getGraphTypeEnum())) {
            if (ObjectUtils.isEmpty(areaDto.getCenterLat())) {
                result.resultFail("当区域类型为圆时，中心纬度必填");
                return true;
            }
            if (ObjectUtils.isEmpty(areaDto.getCenterLng())) {
                result.resultFail("当区域类型为圆时，中心经度必填");
                return true;
            }
            if (ObjectUtils.isEmpty(areaDto.getRadius())) {
                result.resultFail("当区域类型为圆时，半径必填");
                return true;
            }
        }
        if (GraphTypeEnum.POLYGON.equals(areaDto.getGraphTypeEnum())) {
            InAreaPointsDto[] areaPoints = areaDto.getAreaPoints();
            if (areaPoints == null || areaPoints.length == 0) {
                result.resultFail("当区域类型为多边形时，区域点位集合必填");
                return true;
            }
        }
        return false;
    }


}
