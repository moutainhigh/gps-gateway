package com.zkkj.gps.gateway.ccs.service.impl;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.ccs.config.CommonBaseUtil;
import com.zkkj.gps.gateway.ccs.config.RedisDao;
import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmConfigDbDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.TerminalAreaDto;
import com.zkkj.gps.gateway.ccs.dto.token.TokenUser;
import com.zkkj.gps.gateway.ccs.dto.token.TruckAndTerminal;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsObtain;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsTruck;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealTruckPosition;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.exception.TimeValidException;
import com.zkkj.gps.gateway.ccs.mappper.GpsInternalMapper;
import com.zkkj.gps.gateway.ccs.service.GpsInternalService;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.ccs.service.LocationWebApiService;
import com.zkkj.gps.gateway.ccs.utils.BeanValidate;
import com.zkkj.gps.gateway.ccs.utils.GPSPositionUtils;
import com.zkkj.gps.gateway.ccs.utils.HttpExternal;
import com.zkkj.gps.gateway.ccs.utils.JSonUtils;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.controller.TerminalMonitorController;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.*;

/**
 * author : cyc
 * Date : 2019-05-16
 */
@Service
public class GpsInternalServiceImpl implements GpsInternalService {
    @Value("${out.line.time}")
    private int outLineTime;
    @Value("${valid.time}")
    private int validTime;
    @Autowired
    private IGenerator iGenerator;

    @Autowired
    private RedisDao redisDao;

    private TerminalMonitorController terminalMonitorController;

    @Autowired
    private LocationWebApiService locationWebApiService;

    @PostConstruct
    private void init() {
        if (terminalMonitorController == null) {
            terminalMonitorController = new TerminalMonitorController();
        }

    }

    @Autowired
    private GpsInternalMapper gpsInternalMapper;

    @Autowired
    private CommonBaseUtil commonBaseUtil;


    @Override
    public void positionChange(String terminalId, BaseGPSPositionDto baseGPSPositionDto, String dispatchNo) throws Exception {
        terminalMonitorController.positionChange(terminalId, baseGPSPositionDto, dispatchNo);
    }

    /**
     * 持久化原始点位数据
     *
     * @param vehicleLocationHisDto
     */
    @Override
    public void saveOriginalGpsInfo(VehicleLocationHisDto vehicleLocationHisDto) {
        gpsInternalMapper.saveOriginalGpsInfo(vehicleLocationHisDto);
    }

    @Override
    public int getVehicleLocationCount(String terminalId) {
        return gpsInternalMapper.getVehicleLocationCount(terminalId);
    }

    @Override
    public void saveFilterGpsInfo(VehicleLocationHisDto vehicleLocationDto) {
        gpsInternalMapper.saveFilterGpsInfo(vehicleLocationDto);
    }

    @Override
    public void updateFilterGpsInfo(VehicleLocationHisDto vehicleLocationDto) {
        gpsInternalMapper.updateFilterGpsInfo(vehicleLocationDto);
    }

    @Override
    public void batchSaveOriginalGpsInfo(List<VehicleLocationHisDto> vehicleLocationHisDtoList) throws Exception {
        int count = gpsInternalMapper.batchSaveOriginalGpsInfo(vehicleLocationHisDtoList);
    }

    @Override
    public List<VehicleLocationHisDto> getLatestPositionList() {
        return gpsInternalMapper.getLatestPositionList();
    }

    @Override
    public List<GPSPositionDto> getLatestGPSPositionList(List<String> simIdList) {
        List<GPSPositionDto> latestGPSPositionList = Lists.newArrayList();
        Map<String, BaseGPSPositionDto> cacheGPSPosition = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
        String token = HttpExternal.getToken();
        String tokenStr = redisDao.getValue(token);
        TokenUser tokenUser = JSonUtils.readValue(tokenStr, TokenUser.class);
        if (tokenUser != null && tokenUser.getTruckAndTerminalList() != null && tokenUser.getTruckAndTerminalList().size() > 0) {
            Map<String, String> map = new HashMap<>();
            for (TruckAndTerminal truckAndTerminal : tokenUser.getTruckAndTerminalList()) {
                if (!ObjectUtils.isEmpty(truckAndTerminal.getTerminalNo()) && !ObjectUtils.isEmpty(truckAndTerminal.getTruckNo())) {
                    map.put(truckAndTerminal.getTerminalNo(), truckAndTerminal.getTruckNo());
                }
            }
            if (MapUtils.isNotEmpty(cacheGPSPosition)) {
                for (String simId : simIdList) {
                    BaseGPSPositionDto baseGPSPositionDto = cacheGPSPosition.get(simId);
                    if (baseGPSPositionDto != null && baseGPSPositionDto.getPoint() != null) {
                        BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
                        GPSPositionDto position = new GPSPositionDto();
                        position.setAcc(basicPositionDto.getAcc());
                        position.setAlarmState(basicPositionDto.getAlarmState() == null ? 0 : basicPositionDto.getAlarmState());
                        position.setCourse(basicPositionDto.getDirection() == null ? 0 : basicPositionDto.getDirection());
                        position.setElevation(basicPositionDto.getElevation() == null ? 0 : basicPositionDto.getElevation());
                        position.setLatitude(basicPositionDto.getLatitude() == null ? 0.0 : basicPositionDto.getLatitude());
                        position.setLongitude(basicPositionDto.getLongitude() == null ? 0.0 : basicPositionDto.getLongitude());
                        try {
                            position.setLoad(basicPositionDto.getLoad() == null ? 0.0 : Double.valueOf(basicPositionDto.getLoad()));
                        } catch (Exception e) {
                            position.setLoad(0.0);
                        }
                        position.setOilMass(basicPositionDto.getOilMass() == null ? 0 : basicPositionDto.getOilMass());
                        position.setTerminalState(basicPositionDto.getTerminalState() == null ? 0 : basicPositionDto.getTerminalState());
                        position.setPower(basicPositionDto.getPower());
                        position.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
                        position.setRecTime(DateTimeUtils.formatLocalDateTime(baseGPSPositionDto.getRcvTime()));
                        position.setMilesKM(basicPositionDto.getMileage() == null ? 0.0 : basicPositionDto.getMileage());
                        position.setSpeed(basicPositionDto.getSpeed() == null ? 0.0 : basicPositionDto.getSpeed());
                        position.setTruckNo(map.get(simId));
                        position.setSimId(simId);
                        latestGPSPositionList.add(position);
                    }
                }
            }

        }
        return latestGPSPositionList;

    }

    private void getGPSPositionList(List<GPSPositionDto> latestGPSPositionList, Map<String, BaseGPSPositionDto> cacheGPSPosition, TruckAndTerminal truckAndTerminal) {
        BaseGPSPositionDto baseGPSPositionDto = cacheGPSPosition.get(truckAndTerminal.getTerminalNo());
        if (baseGPSPositionDto != null && baseGPSPositionDto.getPoint() != null) {
            BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
            GPSPositionDto position = new GPSPositionDto();
            position.setAcc(basicPositionDto.getAcc());
            position.setAlarmState(basicPositionDto.getAlarmState() == null ? 0 : basicPositionDto.getAlarmState());
            position.setCourse(basicPositionDto.getDirection() == null ? 0 : basicPositionDto.getDirection());
            position.setElevation(basicPositionDto.getElevation() == null ? 0 : basicPositionDto.getElevation());
            position.setLatitude(basicPositionDto.getLatitude() == null ? 0.0 : basicPositionDto.getLatitude());
            position.setLongitude(basicPositionDto.getLongitude() == null ? 0.0 : basicPositionDto.getLongitude());
            try {
                position.setLoad(basicPositionDto.getLoad() == null ? 0.0 : Double.valueOf(basicPositionDto.getLoad()));
            } catch (Exception e) {
                position.setLoad(0.0);
            }
            position.setOilMass(basicPositionDto.getOilMass() == null ? 0 : basicPositionDto.getOilMass());
            position.setTerminalState(basicPositionDto.getTerminalState() == null ? 0 : basicPositionDto.getTerminalState());
            position.setPower(basicPositionDto.getPower());
            position.setGpsTime(DateTimeUtils.formatLocalDateTime(basicPositionDto.getDate()));
            position.setRecTime(DateTimeUtils.formatLocalDateTime(baseGPSPositionDto.getRcvTime()));
            position.setMilesKM(basicPositionDto.getMileage() == null ? 0.0 : basicPositionDto.getMileage());
            position.setSpeed(basicPositionDto.getSpeed() == null ? 0.0 : basicPositionDto.getSpeed());
            position.setTruckNo(truckAndTerminal.getTruckNo());
            position.setSimId(truckAndTerminal.getTerminalNo());
            latestGPSPositionList.add(position);
        }
    }

    @Override
    public List<AlarmConfigDbDto> getLatestAlarmConfig(List<String> terminalId) {
        List<AlarmConfigDbDto> list = new ArrayList<>();
        AlarmConfigDbDto alarmConfigDbDto = null;
        Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig = CarsAlarmConfigCacheSingle.getInstance().getMapAlarmConfig();
        if (CollectionUtils.isNotEmpty(terminalId)) {
            for (String ter : terminalId) {
                List<AppKeyAlarmConfigDto> appKeyAlarmConfigs = mapAlarmConfig.get(ter);
                if (CollectionUtils.isNotEmpty(appKeyAlarmConfigs)) {
                    for (AppKeyAlarmConfigDto appKeyAlarmConfigDto : appKeyAlarmConfigs) {
                        AlarmConfigDto alarmConfig = appKeyAlarmConfigDto.getAlarmConfig();
                        if (null != alarmConfig) {
                            alarmConfigDbDto = new AlarmConfigDbDto();
                            alarmConfigDbDto.setTerminalId(terminalId.get(0));
                            alarmConfigDbDto.setConfigValue(alarmConfig.getConfigValue());
                            alarmConfigDbDto.setEndTime(alarmConfig.getEndTime());
                            alarmConfigDbDto.setStartTime(alarmConfig.getStartTime());
                            alarmConfigDbDto.setCustomConfigId(alarmConfig.getCustomAlarmConfigId());
                            alarmConfigDbDto.setCorpName(alarmConfig.getCorpName());
                            alarmConfigDbDto.setIdentity(alarmConfig.getCorpIdentity());
                            alarmConfigDbDto.setAppKey(appKeyAlarmConfigDto.getAppKey());
                            alarmConfigDbDto.setDispatchNo(alarmConfig.getDispatchNo());
                            alarmConfigDbDto.setIsDeliveryArea(alarmConfig.getIsDeliveryArea());
                            alarmConfigDbDto.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(ter));
                            list.add(alarmConfigDbDto);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getAllCarsInArea(InAreaDto area, int outLineTime) {
        List<String> carList = Lists.newArrayList();
        AreaDto areaDTO = iGenerator.convert(area, AreaDto.class);
        String token = HttpExternal.getToken();
        String tokenStr = redisDao.getValue(token);
        TokenUser tokenUser = JSonUtils.readValue(tokenStr, TokenUser.class);
        //获取所有车辆
        List<TruckAndTerminal> truckAndTerminalList = tokenUser.getTruckAndTerminalList();
        if (CollectionUtils.isNotEmpty(truckAndTerminalList)) {
            Map<String, String> map = new HashMap<>();
            for (TruckAndTerminal truckAndTerminal : tokenUser.getTruckAndTerminalList()) {
                if (!ObjectUtils.isEmpty(truckAndTerminal.getTerminalNo()) && !ObjectUtils.isEmpty(truckAndTerminal.getTruckNo())) {
                    map.put(truckAndTerminal.getTerminalNo(), truckAndTerminal.getTruckNo());
                }
            }
            Map<String, BaseGPSPositionDto> latestPositionMap = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            //获取缓存中所有的终端信息
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    BaseGPSPositionDto baseGPSPositionDto = latestPositionMap.get(entry.getKey());
                    if (baseGPSPositionDto != null && baseGPSPositionDto.getPoint() != null) {
                        BasicPositionDto position = baseGPSPositionDto.getPoint();
                        //获取至少在线上的车，点位收到时间与系统时间在配置的时间范围内则表示在线上
                        boolean dateFlag = DateTimeUtils.durationMinutes(position.getDate(), DateTimeUtils.getCurrentLocalDateTime()) < outLineTime;
                        if (dateFlag) {
                            //判断当前点位是否在区域内
                            boolean isInAreaFlag = GPSPositionUtil.checkSingleInArea(new Point2D.Double(position.getLongitude(), position.getLatitude()), areaDTO);
                            if (isInAreaFlag) {
                                carList.add(entry.getValue());
                            }
                        }
                    }
                }
            }
        }
        return carList;
    }

    @Override
    public RealBaseGpsPositionInfo getGPSPositionByCondition(InGpsObtain inGpsObtain) {
        TokenUser tokenUser = commonBaseUtil.getUserInfo();
        RealBaseGpsPositionInfo realBaseGpsPositionInfo = null;
        if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList())) {
            List<TruckAndTerminal> truckAndTerminalList = tokenUser.getTruckAndTerminalList();
            Map<String, BaseGPSPositionDto> latestPositionMap = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            //1.先去根据车牌查询终端编号然后再获取点位
            if (StringUtils.isNotBlank(inGpsObtain.getLicensePlate())) {
                for (TruckAndTerminal truckAndTerminal : truckAndTerminalList) {
                    if (inGpsObtain.getLicensePlate().equals(truckAndTerminal.getTruckNo())) {
                        realBaseGpsPositionInfo = getBaseGpsInfoDto(inGpsObtain, latestPositionMap, truckAndTerminal.getTerminalNo());
                        if (realBaseGpsPositionInfo != null) return realBaseGpsPositionInfo;
                    }
                }
                //2.通过车牌号直接获取点位
                realBaseGpsPositionInfo = getBaseGpsInfoDto(inGpsObtain, latestPositionMap, inGpsObtain.getLicensePlate());
                if (realBaseGpsPositionInfo != null) return realBaseGpsPositionInfo;
            }
            //3.通过手机号直接获取点位
            if (StringUtils.isNotBlank(inGpsObtain.getPhoneNum())) {
                realBaseGpsPositionInfo = getBaseGpsInfoDto(inGpsObtain, latestPositionMap, inGpsObtain.getPhoneNum());
                if (realBaseGpsPositionInfo != null) return realBaseGpsPositionInfo;
            }
        }
        return realBaseGpsPositionInfo;
    }


    /**
     * 1、从用户Token中获取设备号
     * 2、设备号存在查询设备实时定位信息，设备号不存在或者查询不到通过车牌号查询车辆定位信息
     * 3、如果设备号与车牌号同时查不出车辆定位信息则从中间线路查询车辆定位信息
     */
    @Override
    public GPSPositionDto getCurrentPosition(String licensePlate) {
        GPSPositionDto realTruckPosition = null;
        LocalDateTime currentLocalDateTime = DateTimeUtils.getCurrentLocalDateTime();   //当前时间
        TokenUser tokenUser = commonBaseUtil.getUserInfo(); //获取用户Tokne
        if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList())) { //判断token是否为null，以及车辆信息是否存在
            BaseGPSPositionDto baseGPSPositionDto;
            Map<String, BaseGPSPositionDto> latestPositionMap = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            Optional<TruckAndTerminal> first = tokenUser.getTruckAndTerminalList().stream().filter(x -> licensePlate.equals(x.getTruckNo())).findFirst();
            if (!first.isPresent()) {
                throw new ParamException("车辆不存在该平台下!");
            }
            TruckAndTerminal truckAndTerminal = first.get();
            baseGPSPositionDto = latestPositionMap.get(truckAndTerminal.getTerminalNo());    //用设备查询实时定位
            GPSPositionDto terminalNoRealTruckPosition = GPSPositionUtils.getGPSPositionDto(baseGPSPositionDto);//用来保存设备号查询的定位信息
            if (terminalNoRealTruckPosition != null) {
                realTruckPosition = terminalNoRealTruckPosition;
                realTruckPosition.setTruckNo(truckAndTerminal.getTruckNo());
            }
            baseGPSPositionDto = latestPositionMap.get(licensePlate);    //用车牌查询实时定位
            GPSPositionDto licensePlateRealTruckPosition = GPSPositionUtils.getGPSPositionDto(baseGPSPositionDto);//用来保存车牌号查询的定位信息

            if (licensePlateRealTruckPosition != null) {
                if (ObjectUtils.isEmpty(terminalNoRealTruckPosition)) {  //如果设备号查询数据为null的话
                    realTruckPosition = licensePlateRealTruckPosition;
                    realTruckPosition.setTruckNo(truckAndTerminal.getTruckNo());
                } else {
                    realTruckPosition = isrealTruckPositionTime(terminalNoRealTruckPosition, licensePlateRealTruckPosition);
                }
            }

            //如果点位数据查询为空的话，或者相距时间大于15分钟 从中交线路中查询
            GPSPositionDto zjxlPlateRealTruckPosition = null;
            ResultVo<RealBaseGpsPositionInfo> realPositionZjxl = locationWebApiService.getGpsByLicensePlate(licensePlate);
            if (!ObjectUtils.isEmpty(realPositionZjxl) &&
                    realPositionZjxl.isSuccess() &&
                    !ObjectUtils.isEmpty(realPositionZjxl.getData())) {
                RealBaseGpsPositionInfo realBaseGpsPositionInfo = realPositionZjxl.getData();
                zjxlPlateRealTruckPosition = new GPSPositionDto();
                zjxlPlateRealTruckPosition.setSimId(truckAndTerminal.getTerminalNo());
                zjxlPlateRealTruckPosition.setTruckNo(realBaseGpsPositionInfo.getLicensePlate());
                zjxlPlateRealTruckPosition.setLongitude(realBaseGpsPositionInfo.getLongitude() == null ? 0.0 : realBaseGpsPositionInfo.getLongitude());
                zjxlPlateRealTruckPosition.setLatitude(realBaseGpsPositionInfo.getLatitude() == null ? 0.0 : realBaseGpsPositionInfo.getLatitude());
                zjxlPlateRealTruckPosition.setSpeed(realBaseGpsPositionInfo.getSpeed() == null ? 0 : realBaseGpsPositionInfo.getSpeed());
                zjxlPlateRealTruckPosition.setMilesKM(realBaseGpsPositionInfo.getMileage() == null ? 0 : realBaseGpsPositionInfo.getMileage());
                zjxlPlateRealTruckPosition.setGpsTime(realBaseGpsPositionInfo.getGpsTimeStr());
                zjxlPlateRealTruckPosition.setRecTime(realBaseGpsPositionInfo.getRcvTimeStr());
                zjxlPlateRealTruckPosition.setCourse(realBaseGpsPositionInfo.getDirection() == null ? 0 : realBaseGpsPositionInfo.getDirection());
                if (ObjectUtils.isEmpty(realTruckPosition)) {
                    realTruckPosition = zjxlPlateRealTruckPosition;
                } else {
                    realTruckPosition = isrealTruckPositionTime(realTruckPosition, zjxlPlateRealTruckPosition);
                }
            }
            if (!ObjectUtils.isEmpty(realTruckPosition) && !StringUtils.isEmpty(realTruckPosition.getGpsTime())) {
                if (validTime <= 0) {
                    return realTruckPosition;
                } else {
                    if (DateTimeUtils.durationMinutes(DateTimeUtils.parseLocalDateTime(realTruckPosition.getGpsTime()), currentLocalDateTime) <= validTime) {
                        return realTruckPosition;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
        return realTruckPosition;
    }

    /**
     * 判断实时定位时间准确性，两个定位时间那个距离当前时间较近返回那个
     *
     * @param r1
     * @param r2
     * @return 两个定位时间那个距离当前时间较近返回那个
     */
    public GPSPositionDto isrealTruckPositionTime(GPSPositionDto r1, GPSPositionDto r2) {
        LocalDateTime currentLocalDateTime = DateTimeUtils.getCurrentLocalDateTime();   //当前时间
        LocalDateTime r1lTime = DateTimeUtils.parseLocalDateTime(r1.getGpsTime());
        LocalDateTime r2Time = DateTimeUtils.parseLocalDateTime(r2.getGpsTime());
        long r1Minutes = DateTimeUtils.durationMinutes(currentLocalDateTime, r1lTime);
        long r2Minutes = DateTimeUtils.durationMinutes(currentLocalDateTime, r2Time);
        if (r1Minutes < r2Minutes) {
            return r1;
        } else {
            return r2;
        }
    }


    @Override
    public List<RealTruckPosition> getPositionByLicensePlate(InGpsTruck inGpsTruck) {
        List<RealTruckPosition> realTruckPositionList = null;
        //当前用户所传车辆解析
        if (ObjectUtils.isEmpty(inGpsTruck) || CollectionUtils.isEmpty(inGpsTruck.getLicensePlates())) {
            return realTruckPositionList;
        }
        List<String> licensePlates = inGpsTruck.getLicensePlates();
        TokenUser tokenUser = commonBaseUtil.getUserInfo();
        if (tokenUser != null && CollectionUtils.isNotEmpty(tokenUser.getTruckAndTerminalList())) {
            //验证用户所传车辆是否属于当前用户
            List<TruckAndTerminal> truckAndTerminalList = tokenUser.getTruckAndTerminalList();
            Map<String, BaseGPSPositionDto> latestPositionMap = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition();
            realTruckPositionList = Lists.newArrayList();
            for (TruckAndTerminal truckAndTerminal : truckAndTerminalList) {
                for (String licensePlate : licensePlates) {
                    if (!ObjectUtils.isEmpty(truckAndTerminal) &&
                            !StringUtils.isEmpty(truckAndTerminal.getTruckNo()) &&
                            !StringUtils.isEmpty(licensePlate) &&
                            licensePlate.equals(truckAndTerminal.getTruckNo())) {
                        BaseGPSPositionDto baseGPSPositionDto = latestPositionMap.get(licensePlate);
                        RealTruckPosition realPosition = new RealTruckPosition();
                        realPosition.setLicensePlate(licensePlate);
                        if (!ObjectUtils.isEmpty(baseGPSPositionDto) &&
                                !ObjectUtils.isEmpty(baseGPSPositionDto.getPoint())) {
                            BasicPositionDto basePoint = baseGPSPositionDto.getPoint();
                            BeanUtils.copyProperties(basePoint, realPosition);
                            realPosition.setGpsTime(basePoint.getDate());
                            realPosition.setRcvTime(baseGPSPositionDto.getRcvTime());
                            realPosition.setMileage((double) basePoint.getMileage());
                            realPosition.setSpeed((double) basePoint.getSpeed());
                        } else {
                            switch (inGpsTruck.getReqZjxlFlag()) {
                                case 1://需要从中交兴路获取位置信息
                                    ResultVo<RealBaseGpsPositionInfo> realPositionZjxl = locationWebApiService.getGpsByLicensePlate(licensePlate);
                                    if (!ObjectUtils.isEmpty(realPositionZjxl) &&
                                            realPositionZjxl.isSuccess() &&
                                            !ObjectUtils.isEmpty(realPositionZjxl.getData())) {
                                        RealBaseGpsPositionInfo realBaseGpsPositionInfo = realPositionZjxl.getData();
                                        BeanUtils.copyProperties(realBaseGpsPositionInfo, realPosition);
                                    }
                                    break;
                            }
                        }
                        realTruckPositionList.add(realPosition);
                    }
                }
            }
        }
        return realTruckPositionList;
    }

    @Override
    public Map<Integer, List<String>> judgeTerminalInAreaList(TerminalAreaDto terminalAreaDto) {
        Map<Integer, List<String>> terminalAreaVerificationDtoMap = new HashMap<>();  //要返回的数据
        List<String> zeroTerminalAreaVerificationDtoList = new ArrayList<>();   //保存状态为0的终端  掉线设备
        List<String> oneTerminalAreaVerificationDtoList = new ArrayList<>();   //保存状态为1的终端   不在区域内的设备
        List<String> twoTerminalAreaVerificationDtoList = new ArrayList<>();   //保存状态为2的终端   在线且再区域内的设备
        List<String> threeTerminalAreaVerificationDtoList = new ArrayList<>();   //保存状态为3的终端 终端设备号不存在 的
        BeanValidate.checkParam(terminalAreaDto);   //进行对象数据校验
        String[] terminalIds = terminalAreaDto.getTerminalId().split(",");//获取到所有的终端设备编号
        for (String terminalId : terminalIds) {
            //获取终端点位信息
            BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalId);
            if (ObjectUtils.isEmpty(baseGPSPositionDto) || ObjectUtils.isEmpty(baseGPSPositionDto.getPoint())) {    //判断该终端的设备号是否存在

                threeTerminalAreaVerificationDtoList.add(terminalId);
                continue;
            }
            BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();  //获取终端点位数据
            //判断终端设备是否在线 true：掉线   false：不掉线
            boolean dateFlag = DateTimeUtils.durationMinutes(basicPositionDto.getDate(), DateTimeUtils.getCurrentLocalDateTime()) > outLineTime;
            System.out.println(DateTimeUtils.durationMinutes(basicPositionDto.getDate(), DateTimeUtils.getCurrentLocalDateTime()) + "%%%%%%%%%%%@#$@#$0");
            if (dateFlag) {

                zeroTerminalAreaVerificationDtoList.add(terminalId);
                continue;
            }
            //判断终端设备是否在区域内
            if (!getIsInAreaFlag(terminalAreaDto, basicPositionDto)) {
                oneTerminalAreaVerificationDtoList.add(terminalId);
                continue;
            }
            twoTerminalAreaVerificationDtoList.add(terminalId);

        }
        terminalAreaVerificationDtoMap.put(0, zeroTerminalAreaVerificationDtoList);
        terminalAreaVerificationDtoMap.put(1, oneTerminalAreaVerificationDtoList);
        terminalAreaVerificationDtoMap.put(2, twoTerminalAreaVerificationDtoList);
        terminalAreaVerificationDtoMap.put(3, threeTerminalAreaVerificationDtoList);


        return terminalAreaVerificationDtoMap;
    }

    private RealBaseGpsPositionInfo getBaseGpsInfoDto(InGpsObtain inGpsObtain, Map<String, BaseGPSPositionDto> latestPositionMap, String number) {
        RealBaseGpsPositionInfo realBaseGpsPositionInfo;
        BaseGPSPositionDto baseGPSPositionDto = latestPositionMap.get(number);
        if (baseGPSPositionDto != null && baseGPSPositionDto.getPoint() != null) {
            realBaseGpsPositionInfo = new RealBaseGpsPositionInfo();
            BasicPositionDto basicPositionDto = baseGPSPositionDto.getPoint();
            BeanUtils.copyProperties(basicPositionDto, realBaseGpsPositionInfo);
            realBaseGpsPositionInfo.setGpsTime(basicPositionDto.getDate());
            realBaseGpsPositionInfo.setRcvTime(baseGPSPositionDto.getRcvTime());
            realBaseGpsPositionInfo.setPhoneNum(inGpsObtain.getPhoneNum());
            realBaseGpsPositionInfo.setLicensePlate(inGpsObtain.getLicensePlate());
            return realBaseGpsPositionInfo;
        }
        return null;
    }

    public boolean getIsInAreaFlag(@RequestBody TerminalAreaDto terminalAreaDto, BasicPositionDto basicPositionDto) {
        InAreaDto inArea = terminalAreaDto.getArea();
        AreaDto area = iGenerator.convert(inArea, AreaDto.class);
        // //判断该终端是否在对应区域内 true：在区域内  false：不在区域内
        boolean isInAreaFlag = GPSPositionUtil.checkSingleInArea(new Point2D.Double(basicPositionDto.getLongitude(), basicPositionDto.getLatitude()), area);
        return isInAreaFlag;
    }


}
