package com.zkkj.gps.gateway.jt808tcp.monitor.controller;

import com.zkkj.gps.gateway.jt808tcp.monitor.entity.ResultVo;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.attribute.TerminalAttributeDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.entity.params.TerminalParamsDto;
import com.zkkj.gps.gateway.jt808tcp.monitor.jt808.protocol.hhdpro.dispatch.ElecDispatchInfo;
import com.zkkj.gps.gateway.jt808tcp.monitor.service.ProtocolIssueService;
import com.zkkj.gps.gateway.jt808tcp.monitor.utils.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部标808设备终端相关Api
 * @author suibozhuliu
 */
@Api(description = "部标808设备终端相关接口",value = "TerminalController")
@RequestMapping("jt808")
@RestController
@CrossOrigin
@Slf4j
public class TerminalController {

    @Autowired
    private ProtocolIssueService protocolIssueService;

    @ApiOperation(value = "终端参数查询")
    @GetMapping("/getParameters")
    public ResultVo<TerminalParamsDto> getParameters(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log,terminalId,"终端参数查询参数：终端号：【" + terminalId + "】\n");
        return protocolIssueService.getParameters(terminalId);
    }

    @ApiOperation(value = "终端参数设置")
    @PostMapping("/setParameters")
    public ResultVo<Boolean> setParameters(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                      @RequestBody TerminalParamsDto terminalParamsDto) {
        LoggerUtils.info(log,terminalId,"终端参数设置参数：terminalParamsDto：【" + terminalParamsDto.toString() + "】\n");
        return protocolIssueService.setParameters(terminalId,terminalParamsDto);
    }

    @ApiOperation(value = "下发电子车牌")
    @PostMapping("/issuePlateNum")
    public ResultVo<String> issuePlateNum(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                            @RequestParam(value = "plateNum") @ApiParam(value = "车牌号", required = true) String plateNum) {
        LoggerUtils.info(log,terminalId,"下发电子车牌参数：终端号：【" + terminalId + "】；车牌号：【" + plateNum + "】\n");
        return protocolIssueService.issuePlateNum(terminalId,plateNum);
    }

    @ApiOperation(value = "读取电子车牌")
    @GetMapping("/readPlateNum")
    public ResultVo<String> readPlateNum(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log,terminalId,"读取电子车牌参数：终端号：【" + terminalId + "】\n");
        return protocolIssueService.readPlateNum(terminalId);
    }

    @ApiOperation(value = "下发设备车牌")
    @PostMapping("/setDeviceCarNum")
    public ResultVo<Boolean> setDeviceCarNum(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                       @RequestParam(value = "plateNumber") @ApiParam(value = "车牌号", required = true) String plateNumber) {
        LoggerUtils.info(log,terminalId,"下发设备车牌参数：终端号：【" + terminalId + "】；车牌号：【" + plateNumber + "】\n");
        return protocolIssueService.setDeviceCarNum(terminalId,plateNumber);
    }

    @ApiOperation(value = "读取设备车牌")
    @GetMapping("/getDeviceCarNum")
    public ResultVo<String> getDeviceCarNum(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log,terminalId,"读取设备车牌参数：终端号：【" + terminalId + "】\n");
        return protocolIssueService.getDeviceCarNum(terminalId);
    }

    @ApiOperation(value = "查询终端属性")
    @GetMapping("/getAttributes")
    public ResultVo<TerminalAttributeDto> getAttributes(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log,terminalId,"查询终端属性参数：终端号：【" + terminalId + "】\n");
        return protocolIssueService.getAttribute(terminalId);
    }

    @ApiOperation(value = "读取电子运单")
    @GetMapping("/readBusiness")
    public ResultVo<ElecDispatchInfo> readBusiness(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log,terminalId,"读取电子运单参数：终端号：【" + terminalId + "】\n");
        return protocolIssueService.readBussiness(terminalId);
    }

    @ApiOperation(value = "下发电子运单")
    @PostMapping("/setBusiness")
    public ResultVo<String> setBusiness(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                            @RequestBody ElecDispatchInfo elecDispatchInfo) {
        LoggerUtils.info(log,terminalId,"下发电子运单参数：终端号：【" + terminalId + "】；电子运单数据：【" + elecDispatchInfo.toString() + "】\n");
        return protocolIssueService.setBusiness(terminalId,elecDispatchInfo);
    }

    /*@ApiOperation(value = "位置信息查询")
    @GetMapping("/getTerminalPosition")
    public JT_0201 getTerminalPosition(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        Message message = new Message(MessageId.位置信息查询, terminalId, null);
        //JT_0201 response = (JT_0201) endpoint.sendMessage(message);
        return null;
    }

    @ApiOperation(value = "数据下行透传")
    @PostMapping("/downPassThrough")
    public CommonResult downPassThrough(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                        @RequestBody JT_0900 body) {
        Message message = new Message(MessageId.数据下行透传, terminalId, body);
        //CommonResult response = (CommonResult) endpoint.sendMessage(message);
        return null;
    }

    @ApiOperation(value = "数据上行透传")
    @PostMapping("/upPassThrough")
    public CommonResult upPassThrough(@RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                      @RequestBody JT_0900 body) {
        Message message = new Message(MessageId.数据上行透传, terminalId, body);
        //CommonResult response = (CommonResult) endpoint.sendMessage(message);
        return null;
    }*/

    /*@ApiOperation(value = "终端参数设置")
    @RequestMapping(value = "{terminalId}/setParameters", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setParameters(@PathVariable("terminalId") String terminalId, @RequestBody TerminalParamsDto terminalParamsDto) {
        if (ObjectUtils.isEmpty(terminalParamsDto)){
            return null;
        }
        int heartPeriod = terminalParamsDto.getHeartPeriod();
        int tcpResponseTime = terminalParamsDto.getTcpResponseTime();
        String mainHost = terminalParamsDto.getMainHost();
        String backupsHost = terminalParamsDto.getBackupsHost();
        int mainPort = terminalParamsDto.getMainPort();
        int backupsPort = terminalParamsDto.getBackupsPort();
        String plateNumber = terminalParamsDto.getPlateNumber();
        List<TerminalParameter> parameters = new ArrayList<>();
        TerminalParameter parameter;
        parameter = new TerminalParameter(1,ParameterId.C0x0001,1,BitOperator.integerTo4Bytes(heartPeriod),heartPeriod);
        parameters.add(parameter);
        *//*parameter = new TerminalParameter(2,ParameterId.C0x0002,4,BitOperator.integerTo4Bytes(tcpResponseTime),tcpResponseTime);
        parameters.add(parameter);
        parameter = new TerminalParameter(19,ParameterId.C0x0013,14,HexStringUtils.stringToBytes(mainHost,14,"GBK"),mainHost);
        parameters.add(parameter);*//*
        parameter = new TerminalParameter(23,ParameterId.C0x0017,14,HexStringUtils.stringToBytes(backupsHost,14,"GBK"),backupsHost);
        parameters.add(parameter);
        *//*parameter = new TerminalParameter(24,ParameterId.C0x0018,4,BitOperator.integerTo4Bytes(mainPort),mainPort);
        parameters.add(parameter);
        parameter = new TerminalParameter(25,ParameterId.C0x0019,4,BitOperator.integerTo4Bytes(backupsPort),backupsPort);
        parameters.add(parameter);
        parameter = new TerminalParameter(131,ParameterId.C0x0083,9,HexStringUtils.stringToBytes(plateNumber,9,"GBK"),plateNumber);
        parameters.add(parameter);*//*
        ParameterSetting body = new ParameterSetting();
        body.setParameters(parameters);
        Message message = new Message(MessageId.设置终端参数, terminalId, body);
        CommonResult response = (CommonResult) endpoint.sendMessage(message);
        return response;
    }*/

}