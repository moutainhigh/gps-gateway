package com.zkkj.gps.gateway.ccs.controller;

import static com.zkkj.gps.gateway.common.utils.DateTimeUtils.calculateTimeHour;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisGpsPointQuery;
import com.zkkj.gps.gateway.ccs.utils.CollectionsSortUtil;
import com.zkkj.gps.gateway.common.utils.FastJsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.annotation.NoAccessResponseLogger;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.MonitorInfo;
import com.zkkj.gps.gateway.ccs.dto.page.PageDto;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.HistoryAlarmInfoDto;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsTruckTime;
import com.zkkj.gps.gateway.ccs.entity.inParam.InTruckQuery;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;
import com.zkkj.gps.gateway.ccs.service.PositionService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.ccs.utils.DesiccationPointCCSUtil;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/history")
@CrossOrigin
@Api(value = "历史信息", description = "历史信息")
public class HistoryController extends CommonBaseUtil {

    private Logger logger = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private PositionService positionService;

    @Autowired
    private LocationWebApiService locationWebApiService;

    @ApiOperation(value = "通过车牌号从中交兴路获取历史轨迹信息接口（注：此接口不予对外）", notes = "通过车牌号从中交兴路获取历史轨迹信息接口api（注：此接口不予对外）")
    @PostMapping(value = "/getTruckByLicensePlate")
    @NoAccessResponseLogger
    public ResultVo<HisGpsPositionInfo> getTruckByLicensePlate(@RequestParam @ApiParam(value = "车牌号", required = true) String licensePlates,
                                                               @RequestParam @ApiParam(value = "查询开始时间（注：距结束时间小于24h，格式：yyyy-MM-dd HH:mm:ss）", required = true) String startTime,
                                                               @RequestParam @ApiParam(value = "查询结束时间（注：距开始时间小于24h，格式：yyyy-MM-dd HH:mm:ss）", required = true) String endTime) {
        ResultVo<HisGpsPositionInfo> resultVo = new ResultVo<>();
        try {
            if (DateTimeUtils.durationMillis(DateTimeUtils.parseLocalDateTime(startTime), DateTimeUtils.parseLocalDateTime(endTime)) > 1 * 24 * 60 * 60 * 1000) {
                resultVo.resultFail("日期传入有误，传入的开始时间与结束时间相差应在24h以内！");
                return resultVo;
            }
            ResultVo<HisGpsPositionInfo> hisGpsPositionInfoResultVo = locationWebApiService.getTruckByLicensePlate(licensePlates, startTime, endTime);
            if (hisGpsPositionInfoResultVo != null) {
                BeanUtils.copyProperties(hisGpsPositionInfoResultVo, resultVo);
                return resultVo;
            }
            resultVo.resultFail("获取历史轨迹失败！");
            return resultVo;
        } catch (Exception e) {
            resultVo.resultFail("系统异常:获取历史轨迹失败！");
            logger.error("HistoryController.getTruckByLicensePlate is error：" + e.getMessage());
            return resultVo;
        }
    }

    @ApiOperation(value = "通过车牌号获取车辆轨迹回放", notes = "通过车牌号获取车辆轨迹回放Api")
    @PostMapping(value = "/getHistoricalTrack")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getHistoricalTrack(@RequestParam String licensePlate,
                                                             @RequestParam @ApiParam(value = "查询开始时间（注：格式：yyyy-MM-dd HH:mm:ss）", required = true) String startTime,
                                                             @RequestParam @ApiParam(value = "查询结束时间（注：格式：yyyy-MM-dd HH:mm:ss）", required = true) String endTime) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            if (validate(licensePlate, startTime, endTime, resultVo)) return resultVo;
            List<GPSPositionDto> gpsPositionDtos = positionService.getHistoricalTrackList(licensePlate, startTime, endTime);
            resultVo.setData(gpsPositionDtos);
            resultVo.setTotal(gpsPositionDtos.size());
            if(CollectionUtils.isEmpty(resultVo.getData())){
                resultVo.setSuccess(false);
                resultVo.setMsg("没有定位数据，获取历史位置失败；");
            }
            return resultVo;
        } catch (ParamException e) {
            resultVo.resultFail(e.getMessage());
            logger.error("HistoryController.getHistoricalTrack is error：" + e.getMessage());
            return resultVo;
        } catch (Exception e) {
            resultVo.resultFail("系统异常:获取历史轨迹失败！");
            logger.error("HistoryController.getHistoricalTrack is error：" + e.getMessage());
            return resultVo;
        }
    }


    @ApiOperation(value = "通过车牌号、手机号获取历史轨迹", notes = "通过车牌号、手机号获取历史轨迹")
    @PostMapping(value = "/getTrackByLicencePhoneNum")
    @NoAccessResponseLogger
    public ResultVo<HisGpsPositionInfo> getTrackByLicencePhoneNum(@RequestBody @ApiParam(value = "车牌号、手机号查询历史轨迹模型", required = true) InTruckQuery inTruckQuery) {
        ResultVo<HisGpsPositionInfo> resultVo = new ResultVo<>();
        try {
            if (ObjectUtils.isEmpty(inTruckQuery)) {
                resultVo.resultFail("参数有误，请检查！");
                return resultVo;
            }
            String licensePlate = inTruckQuery.getLicensePlate();
            String phoneNum = inTruckQuery.getPhoneNum();
            if (StringUtils.isEmpty(licensePlate) && StringUtils.isEmpty(phoneNum)) {
                resultVo.resultFail("车牌号、手机号请选填一项！");
                return resultVo;
            }
            String startTime = inTruckQuery.getStartTime();
            String endTime = inTruckQuery.getEndTime();
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                resultVo.resultFail("日期为必须参数！");
                return resultVo;
            }
            double differHour;
            try {
                differHour = calculateTimeHour(startTime, endTime);
                if (differHour < 0) {
                    resultVo.resultFail("日期传入有误，开始时间比结束时间小！");
                    return resultVo;
                }
            } catch (ParseException e) {
                resultVo.resultFail("日期传入有误，请检查！");
                return resultVo;
            }
            HisGpsPositionInfo truckDto = positionService.getTrackByLicencePhoneNum(licensePlate, phoneNum, startTime, endTime);
            if (!ObjectUtils.isEmpty(truckDto)) {
                resultVo.resultSuccess(truckDto);
                resultVo.setMsg("车辆轨迹查询成功！");
            } else {
                resultVo.resultFail("没有符合条件的历史轨迹！");
            }
        } catch (Exception e) {
            resultVo.resultFail("系统异常:历史轨迹查询失败，请重试！");
        }
        return resultVo;
    }

    @ApiOperation(value = "通过批量或单个车牌号获取历史轨迹", notes = "通过批量或单个车牌号获取历史轨迹Api")
    @PostMapping(value = "/getTrackByLicencePlates")
    @NoAccessResponseLogger
    public ResultVo<List<HisGpsPositionInfo>> getTrackByLicencePlates(@RequestBody @ApiParam(value = "批量或单个车牌号获取历史轨迹模型", required = true) InGpsTruckTime inGpsTruckTime) {
        ResultVo<List<HisGpsPositionInfo>> resultVo = new ResultVo<>();
        try {
            if (ObjectUtils.isEmpty(inGpsTruckTime)) {
                resultVo.resultFail("参数有误，请检查！");
                return resultVo;
            }
            List<String> licensePlates = inGpsTruckTime.getLicensePlates();
            if (CollectionUtils.isEmpty(licensePlates)) {
                resultVo.resultFail("车牌号不能为空！");
                return resultVo;
            }
            String startTime = inGpsTruckTime.getStartTime();
            String endTime = inGpsTruckTime.getEndTime();
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                resultVo.resultFail("日期为必须参数！");
                return resultVo;
            }
            double differHour;
            try {
                differHour = calculateTimeHour(startTime, endTime);
                if (differHour < 0) {
                    resultVo.resultFail("日期有误，结束时间比开始时间小！");
                    return resultVo;
                }
            } catch (ParseException e) {
                resultVo.resultFail("日期有误，请检查！");
                return resultVo;
            }
            List<HisGpsPositionInfo> trackByLicencePlates = positionService.getTrackByLicencePlates(licensePlates, startTime, endTime, inGpsTruckTime.getReqZjxlFlag());
            if (!CollectionUtils.isEmpty(trackByLicencePlates)) {
                resultVo.resultSuccess(trackByLicencePlates);
                resultVo.setMsg("车辆轨迹查询成功！");
                resultVo.setTotal(trackByLicencePlates.size());
            } else {
                resultVo.resultFail("没有符合条件的历史轨迹！");
            }
        } catch (Exception e) {
            logger.error("获取历史轨迹异常：【" + e + "】");
            resultVo.resultFail("系统异常:历史轨迹查询失败，请重试！");
        }
        return resultVo;
    }

    @ApiOperation(value = "获取历史报警信息", notes = "获取历史报警信息")
    @PostMapping(value = "/getHistoryAlarmInfoList")
    @NoAccessResponseLogger
    public ResultVo<List<AlarmInfoSocket>> getHistoryAlarmInfoList(@RequestBody HistoryAlarmInfoDto historyAlarmInfoDto) {
        ResultVo<List<AlarmInfoSocket>> resultVo = new ResultVo<>();
        try {
            BeanValidate.checkParam(historyAlarmInfoDto);
            PageHelper.startPage(historyAlarmInfoDto.getPageIndex(), historyAlarmInfoDto.getPageSize());
            List<AlarmInfoSocket> list = positionService.getHistoryAlarmListInfo(historyAlarmInfoDto);
            PageInfo<AlarmInfoSocket> pageInfo = new PageInfo<>(list);
            long total = pageInfo.getTotal();
            resultVo.resultSuccess(list);
            resultVo.setTotal((int) (total));
            resultVo.setMsg("获取历史报警信息成功");
        } catch (ParamException px) {
            resultVo.resultFail("系统异常:" + px.getMessage());
            logger.error("HistoryController.getGroupRouteListInfo is error", px);
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取历史报警失败！");
            logger.error("HistoryController.getGroupRouteListInfo is error", ex);
        }
        return resultVo;
    }

    @ApiOperation(value = "获取历史轨迹信息", notes = "获取历史轨迹信息")
    @PostMapping(value = "/getHistoryPositionByTerminalNo")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getHistoryPositionByTerminalNo(@RequestParam String terminalId, @RequestParam String startTime,
                                                                         @RequestParam String endTime, @RequestParam(required = false) Integer pageIndex, @RequestParam(required = false) Integer pageSize) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            if (validate(terminalId, startTime, endTime, resultVo)) return resultVo;
            TokenUser tokenUser = getUserInfo();
            String truckNo = "";
            if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
                truckNo = tokenUser.getTruckAndTerminalList().stream().filter(x -> x.getTerminalNo().equals(terminalId))
                        .findFirst().orElse(new TruckAndTerminal("", "")).getTruckNo();
                List<GPSPositionDto> list = positionService.getHistoryPositionByTerminalNo(terminalId, startTime, endTime);
                //去燥测试
                List<GPSPositionDto> validPositionList = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size() - 1; i++) {
                        if (DesiccationPointCCSUtil.validatePosition(list.get(i), list.get(i + 1))) {
                            validPositionList.add(list.get(i + 1));
                        }
                    }
                }
                for (GPSPositionDto gpsPositionDto : validPositionList) {
                    gpsPositionDto.setTruckNo(truckNo);
                }
                if (CollectionUtils.isNotEmpty(validPositionList)) {
                    if (pageIndex != null && pageSize != null && pageIndex > 0 && pageSize > 0) {
                        PageDto<GPSPositionDto> pageDto = new PageDto<>(validPositionList, pageIndex, pageSize);
                        resultVo.setTotal(pageDto.getTotalCount());
                        resultVo.resultSuccess(pageDto.getData());
                    } else {
                        resultVo.setTotal(validPositionList.size());
                        resultVo.resultSuccess(validPositionList);
                    }
                    resultVo.setMsg("获取历史轨迹信息成功！");
                }
            } else {
                resultVo.resultFail("未获取到对应轨迹信息");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取历史轨迹失败");
            logger.error("HistoryController.getHistoryPositionByTerminalNo is error terminalId:" + terminalId + ";startTime:"
                    + startTime + ";endTime:" + endTime + ";pageIndex:" + pageIndex + ";pageSize:" + pageSize, ex);
        }
        return resultVo;
    }

    @ApiOperation(value = "获取历史轨迹信息（锡林郭勒业务）", notes = "获取历史轨迹信息")
    @PostMapping(value = "/getHistoryPositionByMultiTerminalNo")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getHistoryPositionByMultiTerminalNo(@RequestBody @ApiParam("查询条件") List<HisGpsPointQuery> hisGpsPointQueryList) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        List<GPSPositionDto> validPositionList = null;
        try {
            validPositionList = new ArrayList<>();
            for (HisGpsPointQuery hisGpsPointQuery : hisGpsPointQueryList) {
                if (validate(hisGpsPointQuery.getTerminalId(), hisGpsPointQuery.getStartTime(), hisGpsPointQuery.getEndTime(), resultVo))
                    continue;
                List<GPSPositionDto> list = positionService.getHistoryPositionByTerminalNo(hisGpsPointQuery.getTerminalId(), hisGpsPointQuery.getStartTime(), hisGpsPointQuery.getEndTime());
                //去燥测试
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size() - 1; i++) {
                        if (DesiccationPointCCSUtil.validatePosition(list.get(i), list.get(i + 1))) {
                            validPositionList.add(list.get(i + 1));
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(validPositionList)) {
                resultVo.setTotal(validPositionList.size());
                resultVo.resultSuccess(validPositionList);
                resultVo.setMsg("获取历史轨迹信息成功！");
            } else {
                resultVo.resultFail("未获取到对应轨迹信息");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取历史轨迹失败");
            logger.error("HistoryController.getHistoryPositionByMultiTerminalNo is error ,param is " + FastJsonUtils.toJSONString(hisGpsPointQueryList), ex);
        }
        return resultVo;
    }

    private boolean validate(String terminalId, String startTime, String endTime, ResultVo<?> resultVo) {
        if (StringUtils.isBlank(terminalId) || "null".equals(terminalId)) {
            resultVo.resultFail("传入设备号或者车牌号有误!");
            return true;
        }
        try {
            if (DateTimeUtils.durationMillis(DateTimeUtils.parseLocalDateTime(startTime), DateTimeUtils.parseLocalDateTime(endTime)) <= 0) {
                resultVo.resultFail("结束时间需大于开始时间");
                return true;
            }
        } catch (Exception e) {
            resultVo.resultFail("开始时间或者结束格式有误,格式:yyyy-MM-dd HH:mm:ss");
            return true;
        }
        return false;
    }


    @ApiOperation(value = "通过车牌号获取车辆历史轨迹信息", notes = "通过车牌号获取车辆历史轨迹信息Api")
    @PostMapping(value = "/getHistoryPositionByTruckNum")
    @NoAccessResponseLogger
    public ResultVo<List<GPSPositionDto>> getHistoryPositionByTruckNum(@RequestParam String truckNum, @RequestParam String startTime,
                                                                       @RequestParam String endTime, @RequestParam int pageIndex, @RequestParam int pageSize) {
        ResultVo<List<GPSPositionDto>> resultVo = new ResultVo<>();
        try {
            if (validate(truckNum, startTime, endTime, resultVo)) return resultVo;
            TokenUser tokenUser = getUserInfo();
            String terminalId = "";
            if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
                terminalId = tokenUser.getTruckAndTerminalList().stream().filter(x -> x.getTruckNo().equals(truckNum))
                        .findFirst().orElse(new TruckAndTerminal("", "")).getTerminalNo();
                if (!ObjectUtils.isEmpty(terminalId)) {
                    List<GPSPositionDto> list = positionService.getHistoryPositionByTerminalNo(terminalId, startTime, endTime);
                    //去燥测试
                    List<GPSPositionDto> validPositionList = new ArrayList<>();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size() - 1; i++) {
                            if (DesiccationPointCCSUtil.validatePosition(list.get(i), list.get(i + 1))) {
                                validPositionList.add(list.get(i + 1));
                            }
                        }
                    }
                    for (GPSPositionDto gpsPositionDto : validPositionList) {
                        gpsPositionDto.setTruckNo(truckNum);
                    }
                    if (CollectionUtils.isNotEmpty(validPositionList)) {
                        PageDto<GPSPositionDto> pageDto = new PageDto<>(validPositionList, pageIndex, pageSize);
                        resultVo.resultSuccess(pageDto.getData());
                        resultVo.setTotal(pageDto.getTotalCount());
                        resultVo.setMsg("获取历史轨迹信息成功");
                    }
                }

            } else {
                resultVo.resultFail("未获取到对应轨迹信息");
            }
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取历史轨迹失败");
            logger.error("HistoryController.getHistoryPositionByTerminalNo is error", ex);
        }
        return resultVo;
    }

    @ApiOperation(value = "app获取单车或多车任务监控列表", notes = "app获取单车或多车任务监控列表")
    @GetMapping(value = "/getTaskMonitorInfoListForApp")
    @NoAccessResponseLogger
    public ResultVo<List<MonitorInfo>> getTaskMonitorInfoListForApp(@ApiParam("设备编号集合") @RequestParam List<String> terminals) {
        ResultVo<List<MonitorInfo>> resultVo = new ResultVo<>();
        try {
            List<MonitorInfo> monitorInfoList = new ArrayList<>();
            List<DispatchInfoDto> dispatchInfoDtoList = new ArrayList<>();
            //从缓存中获取所有有任务的终端信息
            Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
            if (MapUtils.isNotEmpty(dispatchInfoDtoMap) && CollectionUtils.isNotEmpty(terminals)) {
                //获取当前用户绑定的设备编号
                TokenUser tokenUser = getUserInfo();
                if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList()) && StringUtils.isNotBlank(tokenUser.getIdentity())) {
                    //从Token中获取用户分组信息
                    String userIdentity = tokenUser.getIdentity();
                    //从Token中获取所属该用户下的终端列表
                    List<String> terminalNoList = tokenUser.getTruckAndTerminalList().stream().map(x -> x.getTerminalNo()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(terminalNoList)) {
                        for (String terminal : terminals) {
                            DispatchInfoDto dispatchInfoDto = dispatchInfoDtoMap.get(terminal);
                            if (dispatchInfoDto != null && StringUtils.isNotBlank(dispatchInfoDto.getIdentity())) {
                                String[] identityArr = dispatchInfoDto.getIdentity().split(",");
                                if (identityArr != null && identityArr.length > 0) {
                                    ArrayList<String> identityList = Lists.newArrayList(identityArr);
                                    if (!CollectionUtils.isEmpty(identityList) && identityList.contains(userIdentity)) {
                                        dispatchInfoDtoList.add(dispatchInfoDto);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dispatchInfoDtoList)) {
                monitorInfoList = getMonitorInfoList(dispatchInfoDtoList);
            }
            resultVo.resultSuccess(monitorInfoList);
            resultVo.setTotal(monitorInfoList == null ? 0 : monitorInfoList.size());
            resultVo.setMsg("获取成功");
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取任务监控列表失败");
            logger.error("HistoryController.getTaskMonitorInfoListForApp is error" + ex.getMessage());
        }
        return resultVo;
    }

    @ApiOperation(value = "获取任务监控列表", notes = "获取任务监控列表")
    @GetMapping(value = "/getTaskMonitorInfoList")
    @NoAccessResponseLogger
    public ResultVo<List<MonitorInfo>> getTaskMonitorInfoList() {
        ResultVo<List<MonitorInfo>> resultVo = new ResultVo<>();
        try {
            List<MonitorInfo> monitorInfoList = new ArrayList<>();
            List<DispatchInfoDto> dispatchInfoDtoList = new ArrayList<>();
            //从缓存中获取所有有任务的终端信息
            Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
            if (dispatchInfoDtoMap != null && dispatchInfoDtoMap.size() > 0) {
                //获取当前用户绑定的设备编号
                TokenUser tokenUser = getUserInfo();
                if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null &&
                        tokenUser.getTruckAndTerminalList().size() > 0 && !StringUtils.isEmpty(tokenUser.getIdentity())) {
                    //从Token中获取用户分组信息
                    String userIdentity = tokenUser.getIdentity();
                    //从Token中获取所属该用户下的终端列表
                    List<String> terminalNoList = tokenUser.getTruckAndTerminalList().stream().map(x -> x.getTerminalNo()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(terminalNoList)) {
                        for (Map.Entry<String, DispatchInfoDto> entry : dispatchInfoDtoMap.entrySet()) {
                            if (terminalNoList.contains(entry.getKey())) {
                                DispatchInfoDto dispatchInfoDto = entry.getValue();
                                if (!ObjectUtils.isEmpty(dispatchInfoDto) && !StringUtils.isEmpty(dispatchInfoDto.getIdentity())) {
                                    String[] identityArr = dispatchInfoDto.getIdentity().split(",");
                                    if (identityArr != null && identityArr.length > 0) {
                                        ArrayList<String> identityList = Lists.newArrayList(identityArr);
                                        if (!CollectionUtils.isEmpty(identityList) && identityList.contains(userIdentity)) {
                                            dispatchInfoDtoList.add(entry.getValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (dispatchInfoDtoList != null && dispatchInfoDtoList.size() > 0) {
                monitorInfoList = getMonitorInfoList(dispatchInfoDtoList);
            }
            resultVo.resultSuccess(monitorInfoList);
            resultVo.setTotal(monitorInfoList == null ? 0 : monitorInfoList.size());
            resultVo.setMsg("获取成功");
        } catch (Exception ex) {
            resultVo.resultFail("系统异常:获取任务监控列表失败");
            logger.error("HistoryController.getTaskMonitorInfoList is error" + ex.getMessage());
        }
        return resultVo;
    }

    //对象封装
    private List<MonitorInfo> getMonitorInfoList(List<DispatchInfoDto> dispatchInfoDtos) {
        List<MonitorInfo> monitorInfoList = new ArrayList<>();
        if (dispatchInfoDtos != null && dispatchInfoDtos.size() > 0) {
            for (DispatchInfoDto dispatchInfo : dispatchInfoDtos) {
                MonitorInfo monitorInfo = new MonitorInfo();
                monitorInfo.setCarNumber(dispatchInfo.getCarNumber());
                monitorInfo.setConsignerAreaName((dispatchInfo.getConsignerArea() != null && dispatchInfo.getConsignerArea().getAreaName() != null
                        && dispatchInfo.getConsignerArea().getAreaName().length() > 0) ? dispatchInfo.getConsignerArea().getAreaName() : "");
                monitorInfo.setConsignerCorpName(dispatchInfo.getConsignerCorpName());
                monitorInfo.setCreateTime(dispatchInfo.getCreateTime());
                monitorInfo.setDispatchNo(dispatchInfo.getDispatchNo());
                monitorInfo.setDriverMobile(dispatchInfo.getDriverMobile());
                monitorInfo.setDriverName(dispatchInfo.getDriverName());
                monitorInfo.setReceiverAreaName((dispatchInfo.getReceiverArea() != null && dispatchInfo.getReceiverArea().getAreaName() != null &&
                        dispatchInfo.getReceiverArea().getAreaName().length() > 0) ? dispatchInfo.getReceiverArea().getAreaName() : "");
                monitorInfo.setReceiverCorpName(dispatchInfo.getReceiverCorpName());
                monitorInfo.setShipperCorpName(dispatchInfo.getShipperCorpName());
                monitorInfo.setTerminalNo(dispatchInfo.getTerminalNo());
                monitorInfo.setProductName(dispatchInfo.getProductName());
                monitorInfo.setDispatchType(dispatchInfo.getDispatchType());
                monitorInfoList.add(monitorInfo);
            }
        }
        return monitorInfoList;
    }


}
