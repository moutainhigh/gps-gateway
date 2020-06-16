package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.AlarmConfigCache;
import com.zkkj.gps.gateway.ccs.dto.alarmConfig.AlarmConfigOutDto;
import com.zkkj.gps.gateway.ccs.dto.alarmConfig.OutAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.dto.dbDto.EndAlarmConfigDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.BaseUpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.UpdateDispatchInfoDto;

import java.util.List;

/**
 * author : cyc
 * Date : 2019-05-13
 */
public interface AlarmConfigService {


    /**
     * 更新报警配置信息
     *
     * @param outAlarmConfigDto
     * @return
     * @throws Exception
     */
    void updateAlarmConfig(OutAlarmConfigDto outAlarmConfigDto) throws Exception;

    /**
     * 获取数据库的配置信息
     *
     * @return
     */
    List<AlarmConfigCache> getAllAlarmConfigs();

    /**
     * 更新报警配置结束时间
     *
     * @param endAlarmConfigDto
     * @return
     */
    void updateAlarmConfigEndTime(EndAlarmConfigDto endAlarmConfigDto) throws Exception;

    /**
     * @param terminalId
     * @param appKey
     */
    void clearAlarmConfigCache(String terminalId, String appKey, List<String> customConfigIdList) throws Exception;

    /**
     * 根据终端直接清除未结束的报警配置和报警信息
     *  @param terminalIdList
     * @param appKey
     * @param originDispatchNo
     */
    void clearUnEndAlarmInfo(List<String> terminalIdList, String appKey, String originDispatchNo);

    void updateAlarmConfig1(AlarmConfigOutDto alarmConfigOutDto) throws Exception;

    /**
     * 根据运单编号清除报警配置
     *
     * @param updateDispatchInfoDto
     */
    void updateDispatchInfoByDispatchNo(UpdateDispatchInfoDto updateDispatchInfoDto);

    /**
     * 根据appkey+terminalId+dispachNo 获取对应的报警配置信息
     * @param baseUpdateDispatchInfoDto
     * @return
     */
    List<AlarmConfigCache> getAlarmConfigList(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto);
}
