package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.dto.alarmConfig.AlarmConfigOutDto;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dbDto.EndAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.AlarmConfigService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-13
 */
@RestController
@RequestMapping(value = "/alarmConfig")
@CrossOrigin
@Api(value = "报警配置接口", description = "报警配置接口api")
public class AlarmConfigController {

    private Logger logger = LoggerFactory.getLogger(AlarmConfigController.class);

    @Autowired
    private AlarmConfigService alarmService;


    @PostMapping(value = "updateAlarmConfig")
    @ApiOperation(value = "根据appkey更新终端的报警配置", notes = "根据appkey更新终端的报警配置api")
    public ResultVo<Boolean> updateAlarmConfig(@RequestBody AlarmConfigOutDto alarmConfigOutDto) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        try {
            alarmService.updateAlarmConfig1(alarmConfigOutDto);
            resultVo.setMsg("终端报警配置更新成功");
            resultVo.resultSuccess(true);
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("AlarmConfigController.updateAlarmConfig is error", px);
        } catch (Exception e) {
            logger.error("AlarmConfigController.updateAlarmConfig is error", e);
            resultVo.resultFail("系统异常:更新报警配置失败");
        }
        return resultVo;
    }

    @PostMapping(value = "updateAlarmConfigEndTime")
    @ApiOperation(value = "更新报警配置结束时间接口", notes = "更新报警配置结束时间接口api")
    public ResultVo<Boolean> updateAlarmConfigEndTime(@RequestBody EndAlarmConfigDto endAlarmConfigDto) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        try {
            BeanValidate.checkParam(endAlarmConfigDto);
            alarmService.updateAlarmConfigEndTime(endAlarmConfigDto);
            resultVo.setMsg("更新报警配置结束时间成功");
        } catch (ParamException px) {
            logger.error("AlarmConfigController.updateAlarmConfigEndTime is error", px);
            resultVo.resultFail("系统异常:" + px.getMessage());
        } catch (Exception e) {
            logger.error("AlarmConfigController.updateAlarmConfigEndTime is error", e);
            resultVo.resultFail("系统异常:更新报警配置失败");
        }
        return resultVo;
    }

    @PostMapping(value = "clearAlarmConfigCache")
    @ApiOperation(value = "根据appKey和terminalId清空缓存中的报警配置信息, notes = 根据appKey和terminalId清空缓存中的报警配置信息接口api")
    public ResultVo<Boolean> clearAlarmConfigCache(@ApiParam(value = "设备编号") @RequestParam("ternimalId") String ternimalId,
                                                   @ApiParam(value = "appkey") @RequestParam(value = "appKey",required = false)String appKey,
                                                   @ApiParam(value = "报警配置id集合") @RequestBody List<String> customConfigIdList) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        try {
            alarmService.clearAlarmConfigCache(ternimalId, appKey, customConfigIdList);
            resultVo.setMsg("清空缓存中的报警配置信息成功");
        } catch (Exception e) {
            logger.error("AlarmConfigController.clearAlarmConfigCache is error", e);
            resultVo.resultFail("系统异常:清空缓存中的报警配置失败");
        }
        return resultVo;
    }

}
