package com.zkkj.gps.gateway.ccs.service.impl;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.HistoryAlarmInfoDto;
import com.zkkj.gps.gateway.ccs.entity.basePosition.BaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.denoising.DenoisingBean;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.mappper.PositionMapper;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;
import com.zkkj.gps.gateway.ccs.service.PositionService;
import com.zkkj.gps.gateway.ccs.utils.CollectionsSortUtil;
import com.zkkj.gps.gateway.ccs.utils.DesiccationPointCCSUtil;
import com.zkkj.gps.gateway.ccs.utils.GpsArithmetic;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zkkj.gps.gateway.ccs.utils.GpsArithmetic.pointArithmetic;
import static com.zkkj.gps.gateway.common.utils.DateTimeUtils.calculateTimeHour;
import static com.zkkj.gps.gateway.common.utils.DateTimeUtils.getLastDay;

@Slf4j
@Service
public class PositionServiceImpl implements PositionService {

    //点位缺失时间间隔长度验证值
    @Value("${gps.miss.timeinterval}")
    private int gpsTimeInterval;

    @Autowired
    private CommonBaseUtil commonBaseUtil;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private LocationWebApiService locationWebApiService;

    @Override
    public List<AlarmInfoSocket> getHistoryAlarmListInfo(HistoryAlarmInfoDto historyAlarmInfoDto) {
        return positionMapper.getHistoryAlarmListInfo(historyAlarmInfoDto);
    }

    @Override
    public List<GPSPositionDto> getHistoryPositionByTerminalNo(String terminalId, String startTime, String endTime) {
        return positionMapper.getHistoryPositionByTerminalNo(terminalId, startTime, endTime);
    }

    @Override
    public HisGpsPositionInfo getTrackByLicencePhoneNum(String licensePlate, String phoneNum, String startTime, String endTime) {
        HisGpsPositionInfo baseTruckDto = null;
        //从数据库查询车辆轨迹
        List<GPSPositionDto> gpsPositionDtoList = positionMapper.getTrackByLicencePhoneNum(licensePlate, phoneNum, startTime, endTime);
        List<HisBaseGpsPositionInfo> pointList = null;
        if (!CollectionUtils.isEmpty(gpsPositionDtoList)) {
            pointList = Lists.newArrayList();
            //此处需要模型转换
            for (GPSPositionDto positionDto : gpsPositionDtoList) {
                HisBaseGpsPositionInfo baseGpsDto = trackDataTransform(positionDto);
                if (!ObjectUtils.isEmpty(baseGpsDto)) {
                    pointList.add(baseGpsDto);
                }
            }
        }
        if (!CollectionUtils.isEmpty(pointList)) {
            baseTruckDto = new HisGpsPositionInfo();
            baseTruckDto.setLicensePlate(licensePlate);
            baseTruckDto.setPointList(pointList);
        } else {
            //从中交兴路查询车辆轨迹
            ResultVo<HisGpsPositionInfo> zjxlTrackRes = null;
            zjxlTrackRes = isZjxlTrackRes(licensePlate, startTime, endTime);
            if (!ObjectUtils.isEmpty(zjxlTrackRes) && zjxlTrackRes.isSuccess() &&
                    !ObjectUtils.isEmpty(zjxlTrackRes.getData()) && !CollectionUtils.isEmpty(zjxlTrackRes.getData().getPointList())) {
                baseTruckDto = zjxlTrackRes.getData();
            }
        }
        return baseTruckDto;
    }

    public ResultVo<HisGpsPositionInfo> isZjxlTrackRes(String licensePlate, String startTime, String endTime) {
        ResultVo<HisGpsPositionInfo> zjxlTrackRes = null;
        try {
            double differHour = calculateTimeHour(startTime, endTime);
            if (differHour <= 24) {//传入的时间在24小时内
                zjxlTrackRes = locationWebApiService.getTruckByLicensePlate(licensePlate, startTime, endTime);
                return zjxlTrackRes;
            } else {//传入的时间大于24小时
                //每一小时执行一次
                double ceil = Math.ceil(differHour / 24);
                int cycleIndex = 1;
                boolean isFinish = false;
                List<HisBaseGpsPositionInfo> zjxlPointList = Lists.newArrayList();
                while (cycleIndex <= ceil) {
                    try {
                        String endTimeTemp = getLastDay(startTime);
                        double timeHour = calculateTimeHour(endTime, endTimeTemp);
                        if (timeHour >= 0) {
                            endTimeTemp = endTime;
                            isFinish = true;
                        }
                        zjxlTrackRes = locationWebApiService.getTruckByLicensePlate(licensePlate, startTime, endTimeTemp);
                        if (!ObjectUtils.isEmpty(zjxlTrackRes) && zjxlTrackRes.isSuccess() &&
                                !ObjectUtils.isEmpty(zjxlTrackRes.getData()) && !CollectionUtils.isEmpty(zjxlTrackRes.getData().getPointList())) {
                            zjxlPointList.addAll(zjxlTrackRes.getData().getPointList());
                        }
                        if (isFinish) {
                            break;
                        }
                        cycleIndex++;
                        startTime = getLastDay(startTime);
                    } catch (Exception e) {
                        break;
                    }
                }
                if (!CollectionUtils.isEmpty(zjxlPointList)) {
                    zjxlTrackRes = new ResultVo<>();
                    HisGpsPositionInfo hisGpsPositionInfo = new HisGpsPositionInfo();
                    hisGpsPositionInfo.setLicensePlate(licensePlate);
                    hisGpsPositionInfo.setPointList(zjxlPointList);
                    zjxlTrackRes.resultSuccess(hisGpsPositionInfo);
                }
                return zjxlTrackRes;
            }
        } catch (ParseException e) {
            return zjxlTrackRes;
        }

    }

    @Override
    public List<HisGpsPositionInfo> getTrackByLicencePlates(List<String> licensePlates, String startTime, String endTime, int reqZjxlFlag) {
        List<HisGpsPositionInfo> trackDenoisingList = Lists.newArrayList();
        TokenUser tokenUser = commonBaseUtil.getUserInfo();
        if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList())) {
            List<TruckAndTerminal> truckAndTerminalList = tokenUser.getTruckAndTerminalList();
            if (!CollectionUtils.isEmpty(truckAndTerminalList)) {
                List<HisGpsPositionInfo> trackList = Lists.newArrayList();
                for (String licensePlate : licensePlates) {
                    if (StringUtils.isEmpty(licensePlate)) {
                        continue;
                    }
                    for (TruckAndTerminal truckAndTerminal : truckAndTerminalList) {
                        if (!ObjectUtils.isEmpty(truckAndTerminal) && !StringUtils.isEmpty(truckAndTerminal.getTruckNo())) {
                            if (licensePlate.equals(truckAndTerminal.getTruckNo())) {
                                //从数据库中根据车牌号码查询车辆轨迹信息
                                List<GPSPositionDto> gpsTrackDtoList = positionMapper.getTrackByLicencePlates(licensePlate, startTime, endTime);
                                List<HisBaseGpsPositionInfo> resPointList = Lists.newArrayList();
                                if (!CollectionUtils.isEmpty(gpsTrackDtoList)) {
                                    for (GPSPositionDto gpsPositionDto : gpsTrackDtoList) {
                                        HisBaseGpsPositionInfo baseGpsDto = trackDataTransform(gpsPositionDto);
                                        resPointList.add(baseGpsDto);
                                    }
                                }
                                if (reqZjxlFlag == 1) {//需要从中交兴路获取遗漏的点位信息
                                    //从中交兴路查询遗漏的车辆轨迹信息
                                    //1.验证在数据库中查询出的点位中存在缺失点位的时间段集合
                                    List<GpsArithmetic.TimeBean> timeBeansList = pointArithmetic(resPointList, startTime, endTime, gpsTimeInterval);
                                    //2.当前查询时间范围内内位置信息遗漏的时间段列表与不存在位置信息的时间范围列表进行判断
                                    if (!CollectionUtils.isEmpty(timeBeansList)) {
                                        for (GpsArithmetic.TimeBean timeBean : timeBeansList) {
                                            ResultVo<HisGpsPositionInfo> zjxlTrackRes = locationWebApiService.getTruckByLicensePlate(licensePlate, timeBean.getStartTime(), timeBean.getEndTime());
                                            if (!ObjectUtils.isEmpty(zjxlTrackRes) && zjxlTrackRes.isSuccess() &&
                                                    !ObjectUtils.isEmpty(zjxlTrackRes.getData()) &&
                                                    !CollectionUtils.isEmpty(zjxlTrackRes.getData().getPointList())) {
                                                resPointList.addAll(zjxlTrackRes.getData().getPointList());
                                            }
                                        }
                                    }
                                }
                                HisGpsPositionInfo trackBean = new HisGpsPositionInfo();
                                trackBean.setLicensePlate(licensePlate);
                                trackBean.setPointList(resPointList);
                                trackList.add(trackBean);
                                break;
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(trackList)) {
                    for (int i = 0; i < trackList.size(); i++) {
                        try {
                            if (!CollectionUtils.isEmpty(trackList.get(i).getPointList())) {
                                List<? extends HisBaseGpsPositionInfo> pointList = trackList.get(i).getPointList();
                                if (!CollectionUtils.isEmpty(pointList)) {
                                    List<HisBaseGpsPositionInfo> pointDenoisingList = Lists.newArrayList();
                                    for (int m = 0; m < pointList.size() - 1; m++) {
                                        try {
                                            DenoisingBean denoisingBeanOld = new DenoisingBean();
                                            BeanUtils.copyProperties(pointList.get(m), denoisingBeanOld);
                                            denoisingBeanOld.setSpeedKm(pointList.get(m).getSpeedKm());
                                            DenoisingBean denoisingBeanNew = new DenoisingBean();
                                            BeanUtils.copyProperties(pointList.get(m + 1), denoisingBeanNew);
                                            denoisingBeanNew.setSpeedKm(pointList.get(m + 1).getSpeedKm());
                                            if (DesiccationPointCCSUtil.validatePositionNew(denoisingBeanOld, denoisingBeanNew)) {
                                                pointDenoisingList.add(pointList.get(m + 1));
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                    trackList.get(i).setPointList(pointDenoisingList);
                                }
                            }
                            trackDenoisingList.add(trackList.get(i));
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return trackDenoisingList;
    }

    @Override
    public List<GPSPositionDto> getHistoricalTrackList(String licensePlate, String startTime, String endTime) {
        List<GPSPositionDto> historyPositionByTerminalNo = null;
        TokenUser tokenUser = commonBaseUtil.getUserInfo(); //获取当前用户Token

        //判断用户信息是否正确
        if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList())) {
            //获取当前用户传入车牌号所对应的设备号
            Optional<TruckAndTerminal> first = tokenUser.getTruckAndTerminalList().stream().filter(x -> licensePlate.equals(x.getTruckNo())).findFirst();
            if (!first.isPresent()) {
                throw new ParamException("车辆不存在该平台下");
            }

            TruckAndTerminal truckAndTerminal = first.get();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(truckAndTerminal.getTerminalNo())) {    //使用设备号从缓存中获取市里轨迹
                historyPositionByTerminalNo = getHistoryPositionList(truckAndTerminal.getTerminalNo(), startTime, endTime);
            }
            if (CollectionUtils.isEmpty(historyPositionByTerminalNo)) {
                historyPositionByTerminalNo = getHistoryPositionList(licensePlate, startTime, endTime);
            }
            if (CollectionUtils.isEmpty(historyPositionByTerminalNo)) {
                ResultVo<HisGpsPositionInfo> truckByLicensePlate = isZjxlTrackRes(licensePlate, startTime, endTime);
                if (truckByLicensePlate != null && truckByLicensePlate.isSuccess() && truckByLicensePlate.getData() != null &&
                        CollectionUtils.isNotEmpty(truckByLicensePlate.getData().getPointList())) {
                    List<? extends BaseGpsPositionInfo> pointList = truckByLicensePlate.getData().getPointList();
                    historyPositionByTerminalNo = transformDataToGPSPositionDtos((List<BaseGpsPositionInfo>) pointList, truckAndTerminal);
                }
            }else{
                List<HisBaseGpsPositionInfo> resPointList = Lists.newArrayList();   //所有点位集合
                if (!CollectionUtils.isEmpty(historyPositionByTerminalNo)) {
                    for (GPSPositionDto gpsPositionDto : historyPositionByTerminalNo) {
                        HisBaseGpsPositionInfo baseGpsDto = trackDataTransform(gpsPositionDto);
                        resPointList.add(baseGpsDto);
                    }
                }
                List<GpsArithmetic.TimeBean> timeBeansList = pointArithmetic(resPointList, startTime, endTime, gpsTimeInterval);    //判断那个时间点缺失点位
                //从中交兴路查询遗漏的车辆轨迹信息
                //1.验证在数据库中查询出的点位中存在缺失点位的时间段集合
                if (!CollectionUtils.isEmpty(timeBeansList)) {
                    for (GpsArithmetic.TimeBean timeBean : timeBeansList) {
                        ResultVo<HisGpsPositionInfo> zjxlTrackRes = locationWebApiService.getTruckByLicensePlate(licensePlate, timeBean.getStartTime(), timeBean.getEndTime());
                        if (!ObjectUtils.isEmpty(zjxlTrackRes) && zjxlTrackRes.isSuccess() &&
                                !ObjectUtils.isEmpty(zjxlTrackRes.getData()) &&
                                !CollectionUtils.isEmpty(zjxlTrackRes.getData().getPointList())) {
                            resPointList.addAll(zjxlTrackRes.getData().getPointList());
                        }
                    }
                    historyPositionByTerminalNo=transformDataToGPSPositionDtos(resPointList,truckAndTerminal);
                }
            }
        }
        return historyPositionByTerminalNo;

    }

    private List<GPSPositionDto> transformDataToGPSPositionDtos(List<? extends  BaseGpsPositionInfo> pointList, TruckAndTerminal truckAndTerminal) {
        List<GPSPositionDto> gpsPositionDtos = null;
        if (CollectionUtils.isNotEmpty(pointList)) {
            gpsPositionDtos = Lists.newArrayList();
            for (BaseGpsPositionInfo baseGpsPositionInfo : pointList) {
                GPSPositionDto gpsPositionDto = new GPSPositionDto();
                gpsPositionDto.setSimId(truckAndTerminal.getTerminalNo());
                gpsPositionDto.setTruckNo(truckAndTerminal.getTruckNo());
                gpsPositionDto.setLongitude(baseGpsPositionInfo.getLongitude() == null ? 0 : baseGpsPositionInfo.getLongitude());
                gpsPositionDto.setLatitude(baseGpsPositionInfo.getLatitude() == null ? 0 : baseGpsPositionInfo.getLatitude());
                gpsPositionDto.setSpeed(baseGpsPositionInfo.getSpeed() == null ? 0 : baseGpsPositionInfo.getSpeed());
                gpsPositionDto.setMilesKM(baseGpsPositionInfo.getMileage() == null ? 0 : baseGpsPositionInfo.getMileage());
                gpsPositionDto.setRecTime(baseGpsPositionInfo.getRcvTimeStr());
                gpsPositionDto.setGpsTime(baseGpsPositionInfo.getGpsTimeStr());
                gpsPositionDto.setCourse(baseGpsPositionInfo.getDirection() == null ? 0 : baseGpsPositionInfo.getDirection());
                gpsPositionDtos.add(gpsPositionDto);
            }
        }
        return gpsPositionDtos;
    }

    private List<GPSPositionDto> getHistoryPositionList(String terminalNo, String startTime, String endTime) {
        return positionMapper.getHistoryPositionByTerminalNo(terminalNo, startTime, endTime);
    }


    /**
     * 轨迹点模型转换
     *
     * @param positionDto
     * @return
     */
    private HisBaseGpsPositionInfo trackDataTransform(GPSPositionDto positionDto) {
        HisBaseGpsPositionInfo baseGpsDto = null;
        if (!ObjectUtils.isEmpty(positionDto)) {
            baseGpsDto = new HisBaseGpsPositionInfo();
            baseGpsDto.setLongitude(positionDto.getLongitude());
            baseGpsDto.setLatitude(positionDto.getLatitude());
            baseGpsDto.setDirection(positionDto.getCourse());
            baseGpsDto.setElevation(positionDto.getElevation());
            baseGpsDto.setSpeed(positionDto.getSpeed());
            baseGpsDto.setMileage(positionDto.getMilesKM());
            try {
                baseGpsDto.setGpsTime(DateTimeUtils.parseLocalDateTime(positionDto.getGpsTime()));
            } catch (Exception e) {
                baseGpsDto.setGpsTime(null);
            }
            try {
                baseGpsDto.setRcvTime(DateTimeUtils.parseLocalDateTime(positionDto.getRecTime()));
            } catch (Exception e) {
                baseGpsDto.setRcvTime(null);
            }
            baseGpsDto.setGpsTimeStr(positionDto.getGpsTime());
            baseGpsDto.setRcvTimeStr(positionDto.getRecTime());
        }
        return baseGpsDto;
    }

}
