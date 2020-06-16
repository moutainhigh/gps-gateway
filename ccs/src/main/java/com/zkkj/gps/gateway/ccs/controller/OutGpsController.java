package com.zkkj.gps.gateway.ccs.controller;

import java.util.ArrayList;
import java.util.List;

import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.OutGpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.OutTerminalParamsInfoDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.TerminalParamsInfoDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.GpsWebApiService;
import com.zkkj.gps.gateway.ccs.service.OutGpsService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.common.constant.BaseConstant;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * author : cyc
 * <p>
 * Date : 2019-06-12
 */
@RestController
@RequestMapping(value = "/outGps")
@CrossOrigin
@Api(value = "外部暴露车载设备相关接口", description = "外部暴露车载设备相关接口api")
public class OutGpsController {

    private Logger logger = LoggerFactory.getLogger(OutGpsController.class);

    @Autowired
    private OutGpsService outGpsService;

    @Autowired
    private GpsWebApiService gpsWebApiService;

    @ApiOperation(value = "下发电子运单接口", notes = "下发电子运单接口api")
    @PostMapping("/setBusiness")
    public ResultVo<String> setBusiness(@ApiParam(value = "向外暴露下发电子运单模型", required = true) @RequestBody OutGpsBusinessDto outGpsBusinessDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            BeanValidate.checkParam(outGpsBusinessDto);
            //对收发运公司名称限制
            GpsBusinessDto gpsBusinessDto = outGpsBusinessDto.getGpsBusinessDto();
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(outGpsBusinessDto.getTerminalId());
            if (baseGPSPositionDto == null) {
                resultVo.resultFail("设备未上线或通道已关闭!");
                return resultVo;
            }
            ResultVo<String> result = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), outGpsBusinessDto.getTerminalId(), gpsBusinessDto);
            if (result != null) {
                BeanUtils.copyProperties(result, resultVo);
            } else {
                resultVo.resultFail("下发电子运单失败");
            }
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("OutGpsController.setBusiness is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:下发电子运单失败");
            logger.error("OutGpsController.setBusiness is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "读取电子运单接口", notes = "读取电子运单接口api")
    @PostMapping("/readBusiness")
    public ResultVo<GpsBusinessDto> readBusiness(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        ResultVo<GpsBusinessDto> resultVo = new ResultVo<>();
        try {
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
            if (baseGPSPositionDto == null) {
                resultVo.resultFail("设备未上线或通道已关闭!");
                return resultVo;
            }
            ResultVo<GpsBusinessDto> result = gpsWebApiService.readBusiness(baseGPSPositionDto.getFlag(), terminalId);
            if (result != null) {
                BeanUtils.copyProperties(result, resultVo);
            } else {
                resultVo.resultFail("读取电子运单失败");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:读取电子运单失败");
            logger.error("OutGpsController.readBusiness is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "设置终端参数接口", notes = "设置终端参数接口api")
    @PostMapping("/setTerminalArgs")
    public ResultVo<String> setTerminalArgs(@ApiParam(value = "向外暴露终端参数信息模型", required = true) @RequestBody OutTerminalParamsInfoDto outTerminalParamsInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            ResultVo<String> result = gpsWebApiService.setTerminalArgs(outTerminalParamsInfoDto.getTerminalId(), outTerminalParamsInfoDto.getTerminalParamsInfoDto());
            if (result != null) {
                BeanUtils.copyProperties(result, resultVo);
            } else {
                resultVo.resultFail("设置终端参数失败");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:设置终端参数失败");
            logger.error("OutGpsController.setTerminalArgs is error", e);
        }
        return resultVo;
    }


    @ApiOperation(value = "获取终端参数信息接口", notes = "获取终端参数信息接口api")
    @PostMapping("/readTerminalArgs")
    public ResultVo<TerminalParamsInfoDto> readTerminalArgs(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        ResultVo<TerminalParamsInfoDto> resultVo = new ResultVo<>();
        try {
            ResultVo<TerminalParamsInfoDto> result = gpsWebApiService.readTerminalArgs(terminalId);
            if (result != null) {
                BeanUtils.copyProperties(result, resultVo);
            } else {
                resultVo.resultFail("获取终端参数信息失败");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:获取终端参数信息失败");
            logger.error("OutGpsController.readTerminalArgs is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "设置电子运单状态接口", notes = "设置电子运单状态接口api")
    @PostMapping("/setBusinessStatus")
    public ResultVo<String> setBusinessStatus(@ApiParam(value = "向外暴露下发电子运单模型", required = true) @RequestBody OutGpsBusinessDto outGpsBusinessDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            BeanValidate.checkParam(outGpsBusinessDto);
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(outGpsBusinessDto.getTerminalId());
            if (baseGPSPositionDto == null) {
                resultVo.resultFail("设备未上线或通道已关闭!");
                return resultVo;
            }
            GpsBusinessDto gpsBusinessDto = outGpsBusinessDto.getGpsBusinessDto();
            if (StringUtils.isBlank(gpsBusinessDto.getStatus())) {
                resultVo.resultFail("电子运单状态不能为空");
                return resultVo;
            }
            //修改数据库电子运单状态
            ResultVo<String> result = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), outGpsBusinessDto.getTerminalId(), gpsBusinessDto);
            //同步
            if (result != null) {
                BeanUtils.copyProperties(result, resultVo);
                if (resultVo.isSuccess()) {
                    outGpsService.setBusinessStatus(outGpsBusinessDto);
                    resultVo.setMsg("设置电子运单状态成功");
                }
            } else {
                resultVo.resultFail("设置电子运单状态失败");
            }
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("OutGpsController.setBusinessStatus is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:设置电子运单状态失败");
            logger.error("OutGpsController.setBusinessStatus is error", e);
        }
        return resultVo;
    }


    @ApiOperation(value = "取消电子运单接口", notes = "取消电子运单接口api")
    @PostMapping("/cancelBusiness")
    public ResultVo<String> cancelBusiness(@RequestBody OutGpsBusinessDto outGpsBusinessDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(outGpsBusinessDto.getTerminalId());
            if (baseGPSPositionDto == null) {
                resultVo.resultFail("设备未上线或通道已关闭!");
                return resultVo;
            }
            GpsBusinessDto gpsBusinessDto = outGpsBusinessDto.getGpsBusinessDto();
            String disPatchNo = gpsBusinessDto.getDisPatchNo();
            gpsBusinessDto.setDisPatchNo("");
            resultVo = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), outGpsBusinessDto.getTerminalId(), gpsBusinessDto);
            if (resultVo != null) {
                if (resultVo.isSuccess()) {
                    outGpsBusinessDto.getGpsBusinessDto().setStatus("7");
                    outGpsBusinessDto.getGpsBusinessDto().setDisPatchNo(disPatchNo);
                    outGpsService.setBusinessStatus(outGpsBusinessDto);
                    resultVo.setMsg("取消电子运单成功");
                }
            } else {
                resultVo.resultFail("取消电子运单失败");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:取消电子运单失败");
            logger.error("OutGpsController.cancelBusiness is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "设置运单矿发量接口", notes = "设置运单矿发量接口api")
    @PostMapping("/setBusinessGrossWeight")
    public ResultVo<String> setBusinessGrossWeight(@RequestBody OutGpsBusinessDto outGpsBusinessDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            BeanValidate.checkParam(outGpsBusinessDto);
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(outGpsBusinessDto.getTerminalId());
            if (baseGPSPositionDto == null) {
                resultVo.resultFail("设备未上线或通道已关闭!");
                return resultVo;
            }
            GpsBusinessDto gpsBusinessDto = outGpsBusinessDto.getGpsBusinessDto();
            resultVo = gpsWebApiService.setBusiness(baseGPSPositionDto.getFlag(), outGpsBusinessDto.getTerminalId(), gpsBusinessDto);
            if (resultVo != null) {
                if (resultVo.isSuccess()) {
                    resultVo.setMsg("设置运单矿发量成功");
                    outGpsService.setBusinessStatus(outGpsBusinessDto);
                }
            } else {
                resultVo.resultFail("设置运单矿发量失败");
            }
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("OutGpsController.setBusinessGrossWeight is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:设置运单矿发量失败");
            logger.error("OutGpsController.setBusinessGrossWeight is error", e);
        }
        return resultVo;
    }


    @ApiOperation(value = "设置扣吨量，扣吨原因以及状态接口", notes = "设置扣吨量，扣吨原因以及状态接口api")
    @PostMapping("/setDeductWeightAndReason")
    public ResultVo<String> setDeductWeightAndReason(@RequestBody OutGpsBusinessDto outGpsBusinessDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        return resultVo;
    }

    @ApiOperation(value = "蓝牙电子锁施封", notes = "蓝牙电子锁施封接口api")
    @GetMapping("/closeLock")
    public ResultVo<String> closeLock(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        ResultVo<String> result = new ResultVo<>();
        try {
            ResultVo<String> resultVo = gpsWebApiService.closeLock(terminalId);
            if (resultVo != null) {
                BeanUtils.copyProperties(resultVo, result);
            } else {
                result.resultFail("蓝牙电子锁施封失败");
            }
        } catch (Exception e) {
            result.resultFail("系统异常:蓝牙电子锁施封失败");
            logger.error("OutGpsController.closeLock is error", e);
        }
        return result;
    }

    @ApiOperation(value = "蓝牙电子锁解封", notes = "蓝牙电子锁解封接口api")
    @GetMapping("/openLock")
    public ResultVo<String> openLock(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        ResultVo<String> result = new ResultVo<>();
        try {
            ResultVo<String> resultVo = gpsWebApiService.openLock(terminalId);
            if (resultVo != null) {
                BeanUtils.copyProperties(resultVo, result);
            } else {
                result.resultFail("蓝牙电子锁解封失败");
            }
        } catch (Exception e) {
            result.resultFail("系统异常:蓝牙电子锁解封失败");
            logger.error("OutGpsController.openLock is error", e);
        }
        return result;
    }

    @ApiOperation(value = "蓝牙设备下发电子车牌", notes = "蓝牙设备下发电子车牌接口api")
    @GetMapping("/issuePlateNum")
    public ResultVo<String> issuePlateNum(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                          @RequestParam(value = "plateNum") @ApiParam(value = "车牌号", required = true) String plateNum) {
        ResultVo<String> result = new ResultVo<>();
        try {
            if (StringUtils.isBlank(plateNum) || !plateNum.matches(BaseConstant.PLATE_NUMBER)) {
                result.resultFail("请输入正确的车牌号!");
                return result;
            }
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
            if (baseGPSPositionDto == null) {
                result.resultFail("设备未上线或通道已关闭!");
                return result;
            }
            if (StringUtils.isEmpty(plateNum)) {
                plateNum = "";
            }
            ResultVo<String> resultVo = gpsWebApiService.issuePlateNum(baseGPSPositionDto.getFlag(), terminalId, plateNum);
            if (resultVo != null) {
                BeanUtils.copyProperties(resultVo, result);
            } else {
                result.resultFail("蓝牙设备下发电子车牌失败");
            }
        } catch (Exception e) {
            result.resultFail("系统异常:蓝牙设备下发电子车牌失败");
            logger.error("OutGpsController.issuePlateNum is error", e);
        }
        return result;
    }

    @ApiOperation(value = "通过设备编号获取设备信息", notes = "通过设备编号获取设备信息")
    @GetMapping("/getTerminalInfoByTerminalId")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getTerminalInfoByTerminalId(@RequestParam(value = "terminalIds") String terminalIds) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            List<com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto> gpsPositionDtoList = new ArrayList<>();
            if (terminalIds != null && terminalIds.length() > 0) {
                String[] terminalIdArray = terminalIds.split(",");
                for (String terminalId : terminalIdArray) {
                    BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
                    if (baseGPSPositionDto != null) {
                        BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
                        GPSPositionDto desGpsPositionDto = new com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto();
                        desGpsPositionDto.setRecTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                        if (basicPositionDto != null) {
                            desGpsPositionDto.setAlarmState(basicPositionDto.getAlarmState());
                            desGpsPositionDto.setTerminalState(basicPositionDto.getTerminalState());
                            desGpsPositionDto.setLatitude(basicPositionDto.getLatitude());
                            desGpsPositionDto.setLongitude(basicPositionDto.getLongitude());
                            desGpsPositionDto.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                            desGpsPositionDto.setCourse(basicPositionDto.getDirection());
                            desGpsPositionDto.setSimId(terminalId);
                            desGpsPositionDto.setPower(basicPositionDto.getPower());
                        }
                        gpsPositionDtoList.add(desGpsPositionDto);
                    }
                }
                resultVo.resultSuccess(gpsPositionDtoList);
            } else {
                resultVo.resultFail("传入的设备编号有误");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:获取设备信息失败");
            logger.error("getTerminalInfoByTerminalId is error", e);
        }
        return resultVo;
    }


    @ApiOperation(value = "蓝牙设备读取电子车牌", notes = "蓝牙设备读取电子车牌接口api")
    @GetMapping("/readPlateNum")
    public ResultVo<String> readPlateNum
            (@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        ResultVo<String> result = new ResultVo<>();
        try {
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
            if (baseGPSPositionDto == null) {
                result.resultFail("设备未上线或通道已关闭!");
                return result;
            }
            ResultVo<String> resultVo = gpsWebApiService.readPlateNum(baseGPSPositionDto.getFlag(), terminalId);
            if (resultVo != null) {
                BeanUtils.copyProperties(resultVo, result);
            } else {
                result.resultFail("蓝牙设备读取电子车牌失败");
            }
        } catch (Exception e) {
            result.resultFail("系统异常:蓝牙设备读取电子车牌失败");
            logger.error("OutGpsController.readPlateNum is error", e);
        }
        return result;
    }


    private boolean validateParams(GpsBusinessDto gpsBusinessDto, ResultVo<String> resultVo) {
        if (StringUtils.isBlank(gpsBusinessDto.getStatus())) {
            resultVo.resultFail("状态不能为空");
            return true;
        }
        if (StringUtils.isBlank(gpsBusinessDto.getDeductWeight())) {
            resultVo.resultFail("扣吨量不能为空");
            return true;
        }
        if (StringUtils.isBlank(gpsBusinessDto.getDeductReason())) {
            resultVo.resultFail("扣吨原因不能为空");
            return true;
        }
        return false;
    }


}
