package com.zkkj.gps.gateway.ccs.mappper;

import java.util.List;

import org.mapstruct.Mapper;

import com.zkkj.gps.gateway.ccs.dto.dbDto.VehicleLocationHisDto;

/**
 * author : cyc
 * Date : 2019-05-16
 */

@Mapper
public interface GpsInternalMapper {

    /**
     * 持久化原始点位信息
     *
     * @param terminalPositionDto
     */
    void saveOriginalGpsInfo(VehicleLocationHisDto terminalPositionDto);

    /**
     * 根据终端id获取最新点位数量
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
     * @param vehicleLocationDto
     */
    void updateFilterGpsInfo(VehicleLocationHisDto vehicleLocationDto);

    /**
     * 批量点位更新测试
     * @param gpsPositionList
     * @return
     */
    int batchSaveOriginalGpsInfo(List<VehicleLocationHisDto> gpsPositionList);

    /**
     * 批量点位新增测试
     * @param insertList
     * @return
     */
    void batchInsertTest(List<VehicleLocationHisDto> insertList);

    /**
     * 获取数据库中最新的终端对应的信息
     *
     * @return
     */
    List<VehicleLocationHisDto> getLatestPositionList();

    /**
     * 根据终端id获取最新点位信息
     * @param terminalId
     * @return
     */
    VehicleLocationHisDto getPositionByTerminalId(String terminalId);
}
