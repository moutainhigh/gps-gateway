package com.zkkj.gps.gateway.ccs.controller;

import com.zkkj.gps.gateway.ccs.annotation.NoLogger;
import com.zkkj.gps.gateway.ccs.annotation.OpenApi;
import com.zkkj.gps.gateway.ccs.annotation.TokenFilter;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.entity.test.PointSource;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.TestService;
import com.zkkj.gps.gateway.ccs.websocket.WebSocketPosition;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.alarmevent.AlarmInfoEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping(value = "/test")
@CrossOrigin
@Api(value = "测试", description = "测试示例")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private TestService testService;

    @NoLogger
    @ApiOperation(value = "导出点位列表接口", notes = "导出点位列表接口api")
    @PostMapping(value = "/exportPositionList")
    @OpenApi
    @TokenFilter
    public ResultVo<String> exportPositionList(@ApiParam(value = "点位来源模型") @RequestBody PointSource pointSource) {
        ResultVo resultVo = new ResultVo();
        try {
            testService.exportPositionList(pointSource);
        } catch (ParamException e) {
            resultVo.resultFail("系统异常:" + e.getMessage());
            logger.error("TestController.exportPositionList is error", e);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:导出点位列表失败");
            logger.error("TestController exportPositionList is error", e);
        }
        return resultVo;
    }

    @NoLogger
    @ApiOperation(value = "excel导入点位(测试模拟设备回传点位)接口", notes = "excel导入点位(测试模拟设备回传点位)接口api")
    @PostMapping(value = "/importPositionList")
    @OpenApi
    @TokenFilter
    public ResultVo<String> importPositionList(@ApiParam(name = "file", value = "导入excel") @RequestParam(value = "file") MultipartFile file,
                                               @ApiParam(name = "dispatchNo", value = "运单编号") String dispatchNo) {
        ResultVo resultVo = new ResultVo();
        try {
            testService.importPositionList(file, dispatchNo);
        } catch (ParamException e) {
            resultVo.resultFail("系统异常:" + e.getMessage());
            logger.error("TestController.importPositionList is error", e);
        } catch (Exception e) {
            resultVo.resultFail("系统异常:点位excel导出失败");
            logger.error("TestController importPositionList is error", e);
        }
        return resultVo;
    }


    @ApiOperation(value = "测试一个swagger！", notes = "说明信息！")
    @PostMapping(value = "/helloworld")
    @NoLogger
    @OpenApi
    @TokenFilter
    public ResultVo<String> getHelloWorld(String arg) {
        ResultVo resultVo = new ResultVo();
        resultVo.resultSuccess("Hello World!" + arg);
        WebSocketPosition.sendAll("this is message");
        return resultVo;
    }


    @PostMapping(value = "testWebsocket")
    @ApiOperation(value = "测试websocket", notes = "测试websocket")
    @NoLogger
    @OpenApi
    @TokenFilter
    public ResultVo<String> testWebsocket(@RequestParam @ApiParam(name = "id", required = true) int id) {
        ResultVo<String> resultVo = new ResultVo<>();
        resultVo.resultSuccess("成功");
        String appkey = "Kd18fa881-5e8a-4e5e-b242-136303811200";
        TerminalAlarmInfoDto terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
        terminalAlarmInfoDTO.setIdentity("*");
        terminalAlarmInfoDTO.setAlarmGroupId("123456464654646464");
        if (id > 0) {
            terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
        } else {
            terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
        }
        terminalAlarmInfoDTO.setIdentity("77777777");
        terminalAlarmInfoDTO.setCorpName("测试");
        terminalAlarmInfoDTO.setTerminalId("95039631301");
        terminalAlarmInfoDTO.setCarNum("陕U10007");
        terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.dateToStrLong(new Date()));
        terminalAlarmInfoDTO.setLongitude(108.881752);
        terminalAlarmInfoDTO.setLatitude(34.1917381);
        terminalAlarmInfoDTO.setAlarmInfo("测试报警信息");
        terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.OVER_SPEED);
        terminalAlarmInfoDTO.setAreaId("0");
        terminalAlarmInfoDTO.setAlarmValue(20);
        terminalAlarmInfoDTO.setAlarmConfigId("111111111");
        terminalAlarmInfoDTO.setAppKey(appkey);
        AlarmInfoEvent alarmInfoEvent = new AlarmInfoEvent(appkey, terminalAlarmInfoDTO);
        EventBus.getDefault().post(alarmInfoEvent);
        return resultVo;
    }


}
