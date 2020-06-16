package com.zkkj.gps.gateway.ccs.mappper;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import com.zkkj.gps.gateway.ccs.dto.websocketDto.AlarmInfoSocket;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.GPSPositionDto;
import com.zkkj.gps.gateway.ccs.dto.websocketDto.HistoryAlarmInfoDto;

@Mapper
public interface PositionMapper {

    /*
     * @Author lx
     * @Description 获取历史报警信息列表
     * @Date 19:14 2019/5/17
     * @Param
     * @return
     **/
    public List<AlarmInfoSocket> getHistoryAlarmListInfo(HistoryAlarmInfoDto historyAlarmInfoDto);
    /*
     * @Author lx
     * @Description 获取当前设备历史轨迹
     * @Description 获取当前设备历史轨迹
     * @Date 9:43 2019/5/22
     * @Param
     * @return
     **/
    public List<GPSPositionDto> getHistoryPositionByTerminalNo(@Param("terminalId")String terminalId,
                                                               @Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 通过手机号、车牌号查询时间段内的点位信息（轨迹）
     * @param licensePlate
     * @param phoneNum
     * @param startTime
     * @param endTime
     * @return
     */
    List<GPSPositionDto> getTrackByLicencePhoneNum(@Param("licensePlate")String licensePlate,@Param("phoneNum")String phoneNum,
                                                   @Param("startTime")String startTime, @Param("endTime")String endTime);

    /**
     * 通过车牌号查询时间段内的轨迹信息
     * @param licensePlate
     * @param startTime
     * @param endTime
     * @return
     */
    List<GPSPositionDto> getTrackByLicencePlates(@Param("licensePlate")String licensePlate,
                                                   @Param("startTime")String startTime, @Param("endTime")String endTime);
}
