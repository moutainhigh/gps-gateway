package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.BaseUpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.UpdateDispatchIdentityInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.UpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.DispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/monitorConfig")
@CrossOrigin
@Api(value = "新增运单时添加对应任务监控信息", description = "新增运单时添加对应报警配置信息api")
public class MonitorConfigController {

    private Logger logger = LoggerFactory.getLogger(MonitorConfigController.class);


    @Autowired
    private DispatchService dispatchService;


    @ApiOperation(value = "手动清除运单(针对运单没有正常结束)接口", notes = "手动清除运单(针对运单没有正常结束)接口api")
    @PostMapping("/clearDispatchInfo")
    public ResultVo<String> clearDispatchInfo(@RequestBody List<BaseUpdateDispatchInfoDto> baseUpdateDispatchInfoDtoList) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.clearDispatchInfo(baseUpdateDispatchInfoDtoList);
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.clearDispatchInfo is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:清除运单失败");
            logger.error("MonitorConfigController.clearDispatchInfo is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "手动添加运单信息到缓存(针对漂移特殊情况)接口", notes = "手动添加运单信息到缓存(针对漂移特殊情况)接口api")
    @PostMapping("/addDispatchInfoCache")
    public ResultVo<String> addDispatchInfoCache(@RequestBody BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.addDispatchInfoCache(baseUpdateDispatchInfoDto);
            resultVo.resultSuccess("将运单信息添加到缓存成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.addDispatchInfoCache is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:运单信息添加到缓存失败");
            logger.error("MonitorConfigController.addDispatchInfoCache is error", e);
        }
        return resultVo;
    }

    @ApiOperation(value = "手动取消或者结束运单接口", notes = "手动取消或者结束运单接口api")
    @PostMapping("/cancelDispatchInfo")
    public ResultVo<String> cancelDispatchInfo(@RequestBody BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.cancelDispatchInfo(baseUpdateDispatchInfoDto);
            resultVo.resultSuccess("取消运单成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.cancelBusiness is error", px);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:终止运单失败");
            logger.error("MonitorConfigController.cancelBusiness is error", e);
        }
        return resultVo;
    }


    @PostMapping(value = "updateDispatchInfoByDispatchNo")
    @ApiOperation(value = "根据运单号修改终端设备编号,针对手机端", notes = "根据运单号修改终端设备编号,针对手机端api")
    public ResultVo<String> updateDispatchInfoByDispatchNo(@RequestBody UpdateDispatchInfoDto updateDispatchInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.updateDispatchInfoByDispatchNo(updateDispatchInfoDto);
            resultVo.resultSuccess("根据运单号修改终端设备编号成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.addMonitorConfigInfo is error", px);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:修改设备编号失败");
            logger.error("MonitorConfigController.addMonitorConfigInfo is error", ex);
        }
        return resultVo;
    }


    @PostMapping(value = "addMonitorConfigInfo")
    @ApiOperation(value = "新增任务监控信息", notes = "新增任务监控api")
    public ResultVo<String> addMonitorConfigInfo(@RequestBody DispatchInfoDto dispatchInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.addMonitorConfigInfo(dispatchInfoDto);
            resultVo.resultSuccess("新增任务监控信息成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.addMonitorConfigInfo is error", px);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:新增任务监控信息失败");
            logger.error("MonitorConfigController.addMonitorConfigInfo is error", ex);
        }
        return resultVo;
    }

    @PostMapping(value = "updateMonitorAffiliation")
    @ApiOperation(value = "更新任务监控所属关系接口(针对2.0派单同步到3.0派单运单所属问题)", notes = "更新任务监控所属关系接口api")
    public ResultVo<String> updateMonitorAffiliation(@RequestBody UpdateDispatchIdentityInfoDto updateDispatchIdentityInfoDto) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            dispatchService.updateMonitorAffiliation(updateDispatchIdentityInfoDto);
            resultVo.resultSuccess("更新任务监控所属关系成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("MonitorConfigController.updateMonitorAffiliation is error", px);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:更新任务监控所属关系失败");
            logger.error("MonitorConfigController.updateMonitorAffiliation is error", ex);
        }
        return resultVo;
    }
}
