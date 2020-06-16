package com.zkkj.gps.gateway.tcp.monitor.app.web;

import com.zkkj.gps.gateway.protocol.dto.base.ResultVo;
import com.zkkj.gps.gateway.tcp.monitor.app.api.GpsWebApi;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.BusinessBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.SyncBean;
import com.zkkj.gps.gateway.tcp.monitor.app.entity.TerminalParams;
import com.zkkj.gps.gateway.tcp.monitor.app.service.JtxApiService;
import com.zkkj.gps.gateway.tcp.monitor.app.service.JtxJt1ApiService;
import com.zkkj.gps.gateway.tcp.monitor.utils.LoggerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author : zkkjgs
 * @Description:
 * @Date: 2019-05-13 下午 7:49
 */
@RestController
@RequestMapping(value = "/gps")
@CrossOrigin
@Api(value = "GpsController", description = "车载设备相关接口")
@Slf4j
public class GpsController extends BaseController{

    /**
     * 航宏达服务
     */
    @Autowired
    private GpsWebApi hhdApiService;

    /**
     * 甲天行远程服务，洛阳大区，jt0
     */
    @Autowired
    private JtxApiService jtxApiService;

    /**
     * 甲天行远程服务，铜川大区，jt1
     */
    @Autowired
    private JtxJt1ApiService jtxJt1ApiService;

    @ApiOperation(value = "下发电子运单")
    @PostMapping("/setBusiness")
    public SyncBean<String> setBusiness(@RequestParam(value = "terminalType") @ApiParam(value = "设备终端类型", required = true) int terminalType,
                                        @RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId,
                                        @RequestBody @ApiParam(value = "电子运单模型", required = true) BusinessBean businessData) {
        LoggerUtils.info(log, terminalId, "下发电子运单参数【设备终端类型：" + terminalType + "；设备终端号：" + terminalId + "；电子运单模型：" + businessData.toString() + "】\n");
        SyncBean<String> result = new SyncBean<>();
        try {
            switch (terminalType){
                //航宏达
                case 1:
                    result = hhdApiService.setBusinessData(terminalId, businessData);
                    break;
                //甲天行
                case 3:
                    String terminalService = getTerminalService(terminalId);
                    if (StringUtils.isEmpty(terminalService)){
                        result.resultFail("设备不在线或通道已关闭！");
                        return result;
                    }
                    switch (terminalService){
                        //洛阳大区
                        case "jt0":
                            result = jtxApiService.setBusiness(terminalId, businessData);
                            break;
                        //铜川大区
                        case "jt1":
                            result = jtxJt1ApiService.setBusiness(terminalId, businessData);
                            break;
                        default:
                            result.resultFail("设备不在线或通道已关闭！");
                            return result;
                    }
                    break;
                default:
                    result.resultFail("设备类型有误，请检查！");
                    return result;
            }
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "下发电子运单异常：【" + e.toString() + "】\n");
            result.resultFail("下发电子运单异常！");
        }
        LoggerUtils.info(log, terminalId, "下发电子运单结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "读取电子运单")
    @PostMapping("/readBusiness")
    public SyncBean<BusinessBean> readBusiness(@RequestParam(value = "terminalType") @ApiParam(value = "设备终端类型", required = true) int terminalType,
                                               @RequestParam(value = "terminalId") @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log, terminalId, "读取电子运单参数【设备终端类型：" + terminalType + "；设备终端号：" + terminalId + "】\n");
        SyncBean<BusinessBean> result = new SyncBean<>();
        try {
            switch (terminalType){
                //航宏达
                case 1:
                    result = hhdApiService.readBusiness(terminalId);
                    break;
                //甲天行
                case 3:
                    String terminalService = getTerminalService(terminalId);
                    if (StringUtils.isEmpty(terminalService)){
                        result.resultFail("设备不在线或通道已关闭！");
                        return result;
                    }
                    switch (terminalService){
                        //洛阳大区
                        case "jt0":
                            result = jtxApiService.readBusiness(terminalId);
                            break;
                        //铜川大区
                        case "jt1":
                            result = jtxJt1ApiService.readBusiness(terminalId);
                            break;
                        default:
                            result.resultFail("设备不在线或通道已关闭！");
                            return result;
                    }
                    break;
                default:
                    result.resultFail("设备类型有误，请检查！");
                    return result;
            }
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "读取电子运单异常：【" + e.toString() + "】\n");
            result.resultFail("读取电子运单异常！");
        }
        LoggerUtils.info(log, terminalId, "读取电子运单结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "下发电子车牌")
    @PostMapping("/issuePlateNum")
    public SyncBean<String> issuePlateNum(@RequestParam(value = "terminalType") @ApiParam(value = "设备终端类型", required = true) int terminalType,
                                          @RequestParam(value = "terminalId")@ApiParam(value = "设备终端号", required = true) String terminalId,
                                          @RequestParam(value = "plateNum",required = false,defaultValue = "") String plateNum) {
        LoggerUtils.info(log, terminalId, "下发电子车牌参数【设备终端类型：" + terminalType + "；设备终端号：" + terminalId + "；车牌号：" + plateNum + "】\n");
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId)) {
            result.resultFail("设备终端号不能为空！");
            return result;
        }
        if (StringUtils.isEmpty(plateNum)) {
            result.resultFail("车牌号不能为空！");
            return result;
        }
        result.resultFail("下发电子车牌失败！");
        try {
            switch (terminalType){
                //航宏达
                case 1:
                    result = hhdApiService.issuePlateNum(terminalId, plateNum);
                    break;
                //甲天行
                case 3:
                    String terminalService = getTerminalService(terminalId);
                    if (StringUtils.isEmpty(terminalService)){
                        result.resultFail("设备不在线或通道已关闭！");
                        return result;
                    }
                    switch (terminalService){
                        //洛阳大区
                        case "jt0":
                            result = jtxApiService.issuePlateNum(terminalId, plateNum);
                            break;
                        //铜川大区
                        case "jt1":
                            result = jtxJt1ApiService.issuePlateNum(terminalId, plateNum);
                            break;
                        default:
                            result.resultFail("设备不在线或通道已关闭！");
                            return result;
                    }
                    break;
                default:
                    result.resultFail("设备类型有误，请检查！");
                    return result;
            }
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "下发电子车牌异常：【" + e.toString() + "】\n");
            result.resultFail("下发电子车牌异常！");
        }
        LoggerUtils.info(log, terminalId, "下发电子车牌结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "读取电子车牌")
    @PostMapping("/readPlateNum")
    public SyncBean<String> readPlateNum(@RequestParam(value = "terminalType") @ApiParam(value = "设备终端类型", required = true) int terminalType,
                                         @RequestParam(value = "terminalId")@ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log, terminalId, "读取电子车牌参数【设备终端类型：" + terminalType + "；设备终端号：" + terminalId + "】\n");
        SyncBean<String> result = new SyncBean<>();
        if (StringUtils.isEmpty(terminalId)) {
            result.resultFail("设备终端号不能为空！");
            return result;
        }
        result.resultFail("电子车牌读取失败！");
        try {
            switch (terminalType){
                //航宏达
                case 1:
                    result = hhdApiService.readPlateNum(terminalId);
                    break;
                //甲天行
                case 3:
                    String terminalService = getTerminalService(terminalId);
                    if (StringUtils.isEmpty(terminalService)){
                        result.resultFail("设备不在线或通道已关闭！");
                        return result;
                    }
                    switch (terminalService){
                        //洛阳大区
                        case "jt0":
                            result = jtxApiService.readPlateNum(terminalId);
                            break;
                        //铜川大区
                        case "jt1":
                            result = jtxJt1ApiService.readPlateNum(terminalId);
                            break;
                        default:
                            result.resultFail("设备不在线或通道已关闭！");
                            return result;
                    }
                    break;
                default:
                    result.resultFail("设备类型有误，请检查！");
                    return result;
            }
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "读取电子车牌异常：【" + e.toString() + "】\n");
            result.resultFail("读取电子车牌异常！");
        }
        LoggerUtils.info(log, terminalId, "读取电子车牌结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "蓝牙电子锁施封")
    @PostMapping("/closeLock")
    public SyncBean<String> closeLock(@RequestParam(value = "terminalId")
                                      @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log, terminalId, "电子锁施封参数【" + "设备终端号：" + terminalId + "】\n");
        SyncBean<String> result = new SyncBean<>();
        result.resultFail("电子锁施封失败！");
        try {
            result = hhdApiService.openOrCloseLock(terminalId, 1);
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "电子锁施封异常：【" + e.toString() + "】\n");
            result.resultFail("电子锁施封异常！");
        }
        LoggerUtils.info(log, terminalId, "电子锁施封结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "蓝牙电子锁解封")
    @PostMapping("/openLock")
    public SyncBean<String> openLock(@RequestParam(value = "terminalId")
                                     @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log, terminalId, "电子锁解封参数【" + "设备终端号：" + terminalId + "】\n");
        SyncBean<String> result = new SyncBean<>();
        result.resultFail("电子锁解封失败！");
        try {
            result = hhdApiService.openOrCloseLock(terminalId, 0);
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "电子锁解封异常：【" + e.toString() + "】\n");
            result.resultFail("电子锁解封异常！");
        }
        LoggerUtils.info(log, terminalId, "电子锁解封结果：【" + result.toString() + "】\n");
        return result;
    }

    @ApiOperation(value = "设置终端参数")
    @PostMapping("/setTerminalArgs")
    public SyncBean<String> setTerminalArgs(@RequestParam(value = "terminalId")
                                            @ApiParam(value = "设备终端号", required = true) String terminalId,
                                            @RequestBody @ApiParam(value = "设备终端参数对象", required = true) TerminalParams terminalParams) {
        LoggerUtils.info(log, terminalId, "设置终端参数【" + "设备终端号：" + terminalId + "；设备终端参数对象" + terminalParams.toString() + "】\n");
        SyncBean<String> result = null;
        try {
            result = hhdApiService.setTerminalArgs(terminalId, terminalParams);
            LoggerUtils.info(log, terminalId, "设置终端参数结果：【" + result.toString() + "】\n");
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "设置终端参数异常：【" + e.toString() + "】\n");
        }
        return result;
    }

    @ApiOperation(value = "获取终端参数")
    @PostMapping("/readTerminalArgs")
    public SyncBean<TerminalParams> readTerminalArgs(@RequestParam(value = "terminalId")
                                                     @ApiParam(value = "设备终端号", required = true) String terminalId) {
        LoggerUtils.info(log, terminalId, "获取终端参数【" + "设备终端号：" + terminalId + "】\n");
        SyncBean<TerminalParams> result = null;
        try {
            result = hhdApiService.readTerminalArgs(terminalId);
            LoggerUtils.info(log, terminalId, "获取终端参数结果：【" + result.toString() + "】\n");
        } catch (Exception e) {
            LoggerUtils.error(log, terminalId, "获取终端参数异常：【" + e.toString() + "】\n");
        }
        return result;
    }

    @ApiOperation(value = "测试hello！", notes = "说明信息！")
    @PostMapping(value = "/hello")
    public ResultVo<String> getHelloWorld(String arg) {
        ResultVo resultVo = new ResultVo();
        resultVo.resultSuccess("Hello World!" + arg);
        return resultVo;
    }

}
