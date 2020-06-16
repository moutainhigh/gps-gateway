package com.zkkj.gps.gateway.ccs.controller;

import static com.zkkj.gps.gateway.common.utils.DateTimeUtils.calculateTimeMinutes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.exception.TimeValidException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmConfigDbDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsObtain;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsTruck;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealTruckPosition;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/latest")
@CrossOrigin
@Api(value = "最新信息", description = "最新信息接口api")
public class LatestController {

    private Logger logger = LoggerFactory.getLogger(LatestController.class);

    @Autowired
    private GpsInternalService gpsInternalService;

    @Autowired
    private LocationWebApiService locationWebApiService;

    //Gps定位时间与当前时间隔间
    @Value("${real.gps.timeinterval}")
    private long timeInterval;

    @ApiOperation(value = "通过车牌号获取车辆定位信息（注：可能会从中交兴路平台获取位置）", notes = "通过车牌号获取车辆定位信息api")
    @PostMapping(value = "/getGpsByLicensePlate")
    @NoAccessResponseLogger
    public ResultVo<List<RealTruckPosition>> getGpsByLicensePlate(@RequestBody InGpsTruck inGpsTruck) {
        ResultVo<List<RealTruckPosition>> resultVo = new ResultVo<>();
        resultVo.resultFail("获取定位失败！");
        try {
            if (ObjectUtils.isEmpty(inGpsTruck) && CollectionUtils.isEmpty(inGpsTruck.getLicensePlates())) {
                resultVo.resultFail("参数有误，请检查！");
                return resultVo;
            }
            List<RealTruckPosition> realTruckPositionList = gpsInternalService.getPositionByLicensePlate(inGpsTruck);
            if (!CollectionUtils.isEmpty(realTruckPositionList)) {
                resultVo.resultSuccess(realTruckPositionList);
                resultVo.setMsg("车辆位置获取成功！");
                resultVo.setTotal(realTruckPositionList.size());
                return resultVo;
            }
        } catch (Exception e) {
            logger.error("LatestController.getPositionByLicensePlate is error", e);
            resultVo.resultFail("系统异常:获取定位失败");
            return resultVo;
        }
        return resultVo;
    }


    @ApiOperation(value = "针对运销宝通过车牌号获取车辆定位信息接口", notes = "针对运销宝通过车牌号获取车辆定位信息接口api")
    @PostMapping(value = "/getCurrentPosition")
    @NoAccessResponseLogger
    public ResultVo<GPSPositionDto> getCurrentPosition(@ApiParam("车牌号") @RequestParam String licensePlate) {
        ResultVo<GPSPositionDto> resultVo = new ResultVo<>();
        resultVo.resultFail("获取定位失败！");
        try {
            if (StringUtils.isBlank(licensePlate)) {
                resultVo.resultFail("传入车牌号有误；");
                return resultVo;
            }
            GPSPositionDto gPSPositionDto = gpsInternalService.getCurrentPosition(licensePlate);
            if (ObjectUtils.isEmpty(gPSPositionDto)) {
                resultVo.resultFail("没有定位数据，获取实时位置失败；");
                return resultVo;
            }
            resultVo.resultSuccess(gPSPositionDto);
            resultVo.setMsg("车辆位置获取成功！");
            return resultVo;
        } catch (ParamException e) {
            logger.error("LatestController.getCurrentPosition is error:params:" + licensePlate + ";errorMessage:" + e.getMessage());
            resultVo.resultFail("参数异常:获取定位失败;");
            return resultVo;
        } catch (TimeValidException e) {
            resultVo.resultFail("系统异常:当前点位已不再规定时间内！");
            logger.error("HistoryController.getHistoricalTrack is error：" + e.getMessage());
            return resultVo;
        } catch (Exception e) {
            logger.error("LatestController.getCurrentPosition is error", e);
            resultVo.resultFail("系统异常:获取定位失败;");
            return resultVo;
        }
    }

    @ApiOperation(value = "根据车牌号,手机号获取车辆是否有定位", notes = "根据车牌号,手机号获取车辆是否有定位api")
    @PostMapping(value = "/getGPSPositionByCondition")
    public ResultVo<RealBaseGpsPositionInfo> getGPSPositionByCondition(@RequestBody InGpsObtain inGpsObtain) {
        ResultVo<RealBaseGpsPositionInfo> resultVo = new ResultVo<>();
        resultVo.resultFail("获取定位失败！");
        try {
            if (inGpsObtain == null || (inGpsObtain != null && StringUtils.isBlank(inGpsObtain.getLicensePlate()) && StringUtils.isBlank(inGpsObtain.getPhoneNum()))) {
                resultVo.resultFail("车牌号或者手机号至少输入一个");
                return resultVo;

            }
            if (StringUtils.isNotBlank(inGpsObtain.getPhoneNum()) && !inGpsObtain.getPhoneNum().matches("^[1]([3-9])[0-9]{9}$")) {
                resultVo.resultFail("请输入正确的11位手机号");
                return resultVo;
            }
            RealBaseGpsPositionInfo realBaseGpsPositionInfo = gpsInternalService.getGPSPositionByCondition(inGpsObtain);
            if (realBaseGpsPositionInfo != null) {
                resultVo.resultSuccess(realBaseGpsPositionInfo);
                resultVo.setMsg("获取定位成功");
                return resultVo;
            }
            //调用外部服务
            if (realBaseGpsPositionInfo == null) {
                if (StringUtils.isNotBlank(inGpsObtain.getLicensePlate())) {
                    ResultVo<RealBaseGpsPositionInfo> gpsByLicensePlateResultVo = locationWebApiService.getGpsByLicensePlate(inGpsObtain.getLicensePlate());
                    if (changeResult(inGpsObtain, resultVo, gpsByLicensePlateResultVo)) return resultVo;

                }
                if (StringUtils.isNotBlank(inGpsObtain.getPhoneNum())) {
                    ResultVo<RealBaseGpsPositionInfo> gpsByphoneNumResultVo = locationWebApiService.getGpsByphoneNum(inGpsObtain.getPhoneNum());
                    if (changeResult(inGpsObtain, resultVo, gpsByphoneNumResultVo)) return resultVo;
                }
            }
        } catch (Exception e) {
            logger.error("LatestController.getGPSPositionByCondition is error", e);
            resultVo.resultFail("系统异常:获取定位失败");
            return resultVo;
        }
        return resultVo;
    }

    private boolean changeResult(InGpsObtain inGpsObtain, ResultVo<RealBaseGpsPositionInfo> resultVo, ResultVo<RealBaseGpsPositionInfo> gpsByphoneNumResultVo) {
        if (gpsByphoneNumResultVo != null && gpsByphoneNumResultVo.isSuccess()) {
            BeanUtils.copyProperties(gpsByphoneNumResultVo, resultVo);
            if (resultVo.getData() != null) {
                resultVo.getData().setPhoneNum(inGpsObtain.getPhoneNum());
                resultVo.getData().setLicensePlate(inGpsObtain.getLicensePlate());
                return true;
            }
        }
        return false;
    }

    @ApiOperation(value = "根据车牌号,手机号获取实时位置", notes = "根据车牌号,手机号获取实时位置接口api")
    @PostMapping(value = "/getRealGpsByLicensePlatePhoneNum")
    public ResultVo<RealBaseGpsPositionInfo> getRealGpsByLicensePlatePhoneNum(@RequestBody InGpsObtain inGpsObtain) {
        ResultVo<RealBaseGpsPositionInfo> resultVo = new ResultVo<>();
        resultVo.resultFail("获取实时定位失败！");
        try {
            if (inGpsObtain == null || (inGpsObtain != null && StringUtils.isBlank(inGpsObtain.getLicensePlate()) && StringUtils.isBlank(inGpsObtain.getPhoneNum()))) {
                resultVo.resultFail("车牌号或者手机号至少输入一个");
                return resultVo;
            }
            if (StringUtils.isNotBlank(inGpsObtain.getPhoneNum()) && !inGpsObtain.getPhoneNum().matches("^[1]([3-9])[0-9]{9}$")) {
                resultVo.resultFail("请输入正确的11位手机号");
                return resultVo;
            }
            RealBaseGpsPositionInfo realGpsDto = gpsInternalService.getGPSPositionByCondition(inGpsObtain);
            if (!ObjectUtils.isEmpty(realGpsDto)) {
                String gpsTimeStr = realGpsDto.getGpsTimeStr();
                if (!StringUtils.isEmpty(gpsTimeStr)) {
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    double differMinutes = calculateTimeMinutes(gpsTimeStr, currentTime);
                    if (differMinutes <= timeInterval) {
                        resultVo.resultSuccess(realGpsDto);
                        resultVo.setMsg("获取定位成功");
                        return resultVo;
                    }
                }
            }
            //调用中交兴路获取点位信息
            if (StringUtils.isNotBlank(inGpsObtain.getLicensePlate())) {
                ResultVo<RealBaseGpsPositionInfo> gpsByLicensePlateResultVo = locationWebApiService.getGpsByLicensePlate(inGpsObtain.getLicensePlate());
                if (changeResult(inGpsObtain, resultVo, gpsByLicensePlateResultVo)) return resultVo;
            }
            //调用手机基站定位获取定位信息
            if (StringUtils.isNotBlank(inGpsObtain.getPhoneNum())) {
                ResultVo<RealBaseGpsPositionInfo> gpsByphoneNumResultVo = locationWebApiService.getGpsByphoneNum(inGpsObtain.getPhoneNum());
                if (changeResult(inGpsObtain, resultVo, gpsByphoneNumResultVo)) return resultVo;
            }
        } catch (Exception e) {
            logger.error("LatestController.getRealGpsByLicensePlatePhoneNum is error", e);
            resultVo.resultFail("系统异常:获取实时位置失败");
        }
        return resultVo;
    }


    @ApiOperation(value = "通过手机号发送定位短信接口", notes = "通过手机号发送定位短信接口api")
    @GetMapping(value = "/sendMsgToAuthlbs")
    public ResultVo<Boolean> sendMsgToAuthlbs(@ApiParam(value = "请输入十一位手机号") @RequestParam String phoneNum) {
        ResultVo<Boolean> resultVo = new ResultVo<>();
        resultVo.resultFail("手机号发送定位短信失败！");
        try {
            if (!phoneNum.matches("^[1]([3-9])[0-9]{9}$")) {
                resultVo.resultFail("请输入正确的11位手机号");
                return resultVo;
            }
            ResultVo<Boolean> sendMsgToAuthlbsResultVo = locationWebApiService.sendMsgToAuthlbs(phoneNum);
            if (sendMsgToAuthlbsResultVo != null) {
                BeanUtils.copyProperties(sendMsgToAuthlbsResultVo, resultVo);
                return resultVo;
            }
        } catch (Exception e) {
            logger.error("LatestController.sendMsgToAuthlbs is error", e);
            resultVo.resultFail("系统异常:发送定位短信失败");
        }
        return resultVo;
    }


    @ApiOperation(value = "通过终端号获取最新位置信息", notes = "通过终端号获取最新位置信息api")
    @PostMapping(value = "/getLatestGPSPositionList")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getLatestGPSPositionList(@RequestBody List<String> simIdList) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            List<GPSPositionDto> list = gpsInternalService.getLatestGPSPositionList(simIdList);
            resultVo.setTotal(list == null ? 0 : list.size());
            resultVo.resultSuccess(list);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取最新点位信息失败");
            logger.error("LatestController.getLatestGPSPositionList is error", ex);
        }
        return resultVo;
    }


    @ApiOperation(value = "通过终端号获取最新的报警配置信息", notes = "通过终端号获取最新的报警配置信息api")
    @PostMapping(value = "/getLatestAlarmConfig")
    @NoAccessResponseLogger
    public ResultVo<List<AlarmConfigDbDto>> getLatestAlarmConfig(@RequestBody List<String> terminalId) {
        ResultVo<List<AlarmConfigDbDto>> resultVo = new ResultVo<>();
        try {
            List<AlarmConfigDbDto> alarmConfig = gpsInternalService.getLatestAlarmConfig(terminalId);
            resultVo.resultSuccess(alarmConfig);
            resultVo.setTotal(alarmConfig == null ? 0 : alarmConfig.size());
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取最新的报警配置失败");
            logger.error("LatestController.getLatestAlarmConfig is error", ex);
        }
        return resultVo;

    }


}
