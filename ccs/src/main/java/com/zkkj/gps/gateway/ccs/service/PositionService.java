package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.common.ResultVo;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.HistoryAlarmInfoDto;
import com.zkkj.gps.gateway.ccs.entity.hisPosition.HisGpsPositionInfo;
import com.zkkj.gps.gateway.ccs.entity.inParam.InGpsTruckTime;

import java.util.List;

public interface PositionService {

    /*
     * @Author lx
     * @Description 获取历史报警信息列表
     * @Date 19:14 2019/5/17
     * @Param
     * @return
     **/
    List<AlarmInfoSocket> getHistoryAlarmListInfo(HistoryAlarmInfoDto historyAlarmInfoDto);

    /*
     * @Author lx
     * @Description 获取当前设备历史轨迹
     * @Description 获取当前设备历史轨迹
     * @Date 9:43 2019/5/22
     * @Param
     * @return
     **/
    List<GPSPositionDto> getHistoryPositionByTerminalNo(String terminalId, String startTime, String endTime);
    /**
     * 通过手机号、车牌号查询历史轨迹
     * @param licensePlate
     * @param phoneNum
     * @param startTime
     * @param endTime
     * @return
     */
    HisGpsPositionInfo getTrackByLicencePhoneNum(String licensePlate, String phoneNum, String startTime, String endTime);

    /**
     * 通过单个、批量车牌号获取车辆历史轨迹
     * @param licensePlates
     * @param startTime
     * @param endTime
     * @return
     */
    List<HisGpsPositionInfo> getTrackByLicencePlates (List<String> licensePlates,String startTime,String endTime,int reqZjxlFlag);

    /**
     * 若车牌与设备有关联，先通过设备id在数据库在数据库查询，没有查询到结果时，使用车牌号在数据库查询，没查到时，通过车牌号在中交兴路接口获取设备历史轨迹
     * @param licensePlate
     * @param startTime
     * @param endTime
     * @return
     */
    //ResultVo<HisGpsPositionInfo> getHistoricalTrack(String licensePlate, String startTime, String endTime);

    /**
     * 根据车牌号获取车辆历史轨迹
     * @param licensePlate  车牌号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 车辆轨迹信息
     */
    List<GPSPositionDto> getHistoricalTrackList(String licensePlate, String startTime, String endTime);
}
