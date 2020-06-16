package com.zkkj.gps.gateway.ccs.mappper;

import com.zkkj.gps.gateway.ccs.dto.AlarmConfigCache;
import com.zkkj.gps.gateway.ccs.dto.dbDto.*;
import com.zkkj.gps.gateway.ccs.dto.dispatch.BaseUpdateDispatchInfoDto;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-13
 */

@Mapper
public interface AlarmConfigMapper {

    /**
     * 批量增加报警配置数据
     *
     * @param list
     */
    void batchSaveAlarmConfig(List<AlarmConfigDbDto> list);


    /**
     * 批量更新区域信息
     *
     * @param areaList
     */
    void batchSaveArea(List<AreaDbDto> areaList);

    /**
     * 批量更新线路信息
     *
     * @param routeList
     */
    void batchSaveRoute(List<RouteDbDto> routeList);

    /**
     * 根据appkey批量删除区域信息
     *
     * @param areaList
     * @param appKey
     */
    void deleteOriginArea(@Param("areaList") List<AreaDbDto> areaList, @Param("appKey") String appKey);

    /**
     * 根据appkey批量删除路线信息
     *
     * @param routeList
     * @param appKey
     */
    void deleteOriginRoute(@Param("routeList") List<RouteDbDto> routeList, @Param("appKey") String appKey);

    /**
     * 获取所有的配置信息
     *
     * @return
     */
    List<AlarmConfigCache> getAllAlarmConfigs();


    /**
     * 根据appkey和customConfigId获取报警配置信息
     *
     * @param appKey
     * @param customConfigId
     * @param terminalId
     * @return
     */
    AlarmConfigDbDto getAlarmConfig(@Param("appKey") String appKey, @Param("customConfigId") String customConfigId, @Param("terminalId") String terminalId);

    /**
     * 更新报警配置结束时间
     *
     * @param endAlarmConfigDto
     * @return
     */
    int updateAlarmConfigEndTime(EndAlarmConfigDto endAlarmConfigDto);

    /**
     * 批量更新报警配置结束时间
     *
     * @param endAlarmConfigDto
     * @param customConfigIdList
     */
    void updateAlarmConfigEndTimeList(@Param("endAlarmConfigDto") EndAlarmConfigDto endAlarmConfigDto, @Param("customConfigIdList") List<String> customConfigIdList);

    /**
     * 移除终端对应的报警配置信息
     *
     * @param terminalIdList
     * @param endTime
     * @param appKey
     */
    void deleteAlarmConfig(@Param("terminalIdList") List<String> terminalIdList, @Param("endTime") String endTime, @Param("appKey") String appKey);

    /**
     * 根据运单，终端编号更新报警配置信息
     * @param updateAlarmConfigDto
     */
    void updateAlarmConfigByDispatchNo(UpdateAlarmConfigDto updateAlarmConfigDto);

    /**
     * 根据appkey+terminalId+dispachNo 获取对应的报警配置信息
     * @param baseUpdateDispatchInfoDto
     * @return
     */
    List<AlarmConfigCache> getAlarmConfigList(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto);
}
