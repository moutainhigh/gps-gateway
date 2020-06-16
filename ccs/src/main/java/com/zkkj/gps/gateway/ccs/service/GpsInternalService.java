package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.dbDto.AlarmConfigDbDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.TerminalAreaDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsObtain;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsTruck;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealBaseGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.realPosition.RealTruckPosition;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * author : cyc
 * Date : 2019-05-16
 */
public interface GpsInternalService {



    /**
     * 更新点位信息
     *
     * @param terminalId
     * @param baseGPSPositionDto
     * @param dispatchNo
     * @return
     */
    void positionChange(String terminalId, BaseGPSPositionDto baseGPSPositionDto, String dispatchNo) throws Exception;

    /**
     * 持久化原始点位信息
     *
     * @param vehicleLocationHisDto
     */
    void saveOriginalGpsInfo(VehicleLocationHisDto vehicleLocationHisDto);

    /**
     * 根据终端id查询到最新点位数量
     *
     * @param terminalId
     * @return
     */
    int getVehicleLocationCount(String terminalId);

    /**
     * 持久化过滤后的最新点位信息
     *
     * @param vehicleLocationDto
     */
    void saveFilterGpsInfo(VehicleLocationHisDto vehicleLocationDto);

    /**
     * 根据终端id更新最新的点位信息
     *
     * @param vehicleLocationDto
     */
    void updateFilterGpsInfo(VehicleLocationHisDto vehicleLocationDto);

    /**
     * 批量点位更新测试
     *
     * @return
     */
    void batchSaveOriginalGpsInfo(List<VehicleLocationHisDto> vehicleLocationHisDtoList) throws Exception;

    /**
     * 获取数据库中最新的终端对应的信息
     *
     * @return
     */
    List<VehicleLocationHisDto> getLatestPositionList();


    /**
     * 根据终端id获取缓存中最新点位信息
     * @param simIdList  type 1设备号 2车牌号
     * @return
     */
    List<GPSPositionDto> getLatestGPSPositionList(List<String> simIdList);

    /**
     * 根据终端id获取最新的报警配置信息
     * @param terminalId
     * @return
     */
    List<AlarmConfigDbDto> getLatestAlarmConfig(List<String> terminalId);

    /**
     * 获取该区域下所有的车辆
     * @param area
     * @param outLineTime
     * @return
     */
    List<String> getAllCarsInArea(InAreaDto area, int outLineTime);

    /**
     * 根据手机号，车牌号获取定位信息
     * @param gpsObtainDto
     * @return
     */
    RealBaseGpsPositionInfo getGPSPositionByCondition(InGpsObtain gpsObtainDto);

    /**
     * 根据车牌获取车辆位置信息
     * @param inGpsTruck
     * @return
     */
    List<RealTruckPosition> getPositionByLicensePlate(InGpsTruck inGpsTruck);

    /**
     * 判断多个终端是否在区域内
     * @param terminalAreaDto
     * @return
     */
    Map<Integer,List<String>> judgeTerminalInAreaList(TerminalAreaDto terminalAreaDto);

    /**
     * 判断终端是否在区域内
     * @param terminalAreaDto   区域模型
     * @param basicPositionDto  当前终端的经纬度信息
     * @return
     */
    boolean getIsInAreaFlag(@RequestBody TerminalAreaDto terminalAreaDto, BasicPositionDto basicPositionDto);

    /**
     * 根据车牌号查询车辆实时定位
     * @param licensePlate  车牌号
     * @return 车辆定位信息
     */
    GPSPositionDto getCurrentPosition(String licensePlate);
}
