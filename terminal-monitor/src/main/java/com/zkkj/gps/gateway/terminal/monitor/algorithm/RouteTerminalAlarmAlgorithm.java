package com.zkkj.gps.gateway.terminal.monitor.algorithm;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.constant.Constant;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.LineStringDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmResTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.CarsAlarmConfigCacheSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author : cyc
 * Date : 2019-04-26
 * 终端线路报警信息
 */
public class RouteTerminalAlarmAlgorithm {

    /**
     * 判断线路偏移
     * 1.获取路线，可能是多条路线
     * 1.1 如果多个点连续的在多个路线上，则没有发生偏移，选择任何一条路线为当前正常路线
     * 1.2 如果多个连续的点没有在任何一条路线上，则发出线路偏移，开始报警
     * 1.3 如果在线路结束之前连续的点在其中的一条路线上，则发出线路偏移结束报警
     * 1.4 如果线路结束还处于线路偏移，则应该发出线路偏移结束报警
     *
     * @param terminalId
     * @param hisListPosition
     * @param appKeyAlarmConfig
     * @return
     */
    public static TerminalAlarmInfoDto terminalAlarmRoute(String terminalId, QueueList<BasicPositionDto> hisListPosition, AppKeyAlarmConfigDto appKeyAlarmConfig) {
        TerminalAlarmInfoDto terminalAlarmInfoDTO = null;
        AlarmConfigDto alarmConfig = appKeyAlarmConfig.getAlarmConfig();
        LineStringDto[] lineStrings = alarmConfig.getLineStrings();
        String startTime = alarmConfig.getStartTime();
        boolean moreThanStartTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, startTime, null);
        if (!alarmConfig.validate().isSuccess()) {
            return terminalAlarmInfoDTO;
        }
        if (!moreThanStartTime) {
            return terminalAlarmInfoDTO;
        }
        List<TerminalAlarmInfoDto> terminalAlarmInfoDTOS = Constant.terminalAlarmInfoCache.get(terminalId) == null ? Lists.newArrayList() : Constant.terminalAlarmInfoCache.get(terminalId);
        //路线偏移中只会出现
        List<TerminalAlarmInfoDto> collect = terminalAlarmInfoDTOS.stream().filter(s -> s.getAlarmConfigId().equals(alarmConfig.getCustomAlarmConfigId()) && s.getAppKey().equals(appKeyAlarmConfig.getAppKey())).collect(Collectors.toList());
        String endTime = alarmConfig.getEndTime();
        boolean moreThanEndTime = GPSPositionUtil.validateHisListPositionTime(hisListPosition, null, endTime);
        if (moreThanStartTime && !moreThanEndTime) {
            //获取所有线路上点到线路的距离
            List<Double> distList = Lists.newArrayList();
            //获取该终端的报警缓存信息
            for (int i = 0; i < lineStrings.length; i++) {
                LineStringDto lineString = lineStrings[i];
                double dist = GPSPositionUtil.checkOnRouteLine(hisListPosition, lineString, alarmConfig.getConfigValue());
                distList.add(dist);
            }
            //多条路线上只要连续的点在一条路线上就说明没有发生路线偏移
            boolean flag = GPSPositionUtil.checkAnyOnRoute(lineStrings);
            /**
             * 需要判断缓存中有没有改终端线路偏移
             * 如果缓存中存在，判断线路偏移结束，则需要发出路线偏移结束报警配置信息
             */
            if (flag && CollectionUtils.isNotEmpty(collect)) {
                terminalAlarmInfoDTO = collect.get(0);
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.end);
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】线路偏移,结束报警");
                terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
                terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
                double maxDist = distList.stream().mapToDouble(s -> s.doubleValue()).summaryStatistics().getMax();
                terminalAlarmInfoDTO.setAlarmValue(maxDist);
                terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
                terminalAlarmInfoDTOS.remove(terminalAlarmInfoDTO);
                Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                return terminalAlarmInfoDTO;
            }
            /**
             * 如果为false，则发生线路偏移
             * 判断缓存中是否有该终端的线路偏移报警
             * 如果有，则不需要报警
             * 如果没有，则需要发出路线偏移报警
             */
            if (!flag && CollectionUtils.isEmpty(collect)) {
                //发出路线偏移报警
                terminalAlarmInfoDTO = new TerminalAlarmInfoDto();
                terminalAlarmInfoDTO.setAlarmGroupId(UUID.randomUUID().toString());
                terminalAlarmInfoDTO.setTerminalId(terminalId);
                terminalAlarmInfoDTO.setAlarmConfigId(alarmConfig.getCustomAlarmConfigId());
                terminalAlarmInfoDTO.setIdentity(alarmConfig.getCorpIdentity());
                terminalAlarmInfoDTO.setCorpName(alarmConfig.getCorpName());
                terminalAlarmInfoDTO.setAlarmResType(AlarmResTypeEnum.start);
                terminalAlarmInfoDTO.setLongitude(hisListPosition.get(0).getLongitude());
                terminalAlarmInfoDTO.setLatitude(hisListPosition.get(0).getLatitude());
                terminalAlarmInfoDTO.setConfigValue(alarmConfig.getConfigValue());
                //2019-06-20添加运单编号
                terminalAlarmInfoDTO.setDispatchNo(alarmConfig.getDispatchNo());
                terminalAlarmInfoDTO.setAlarmTime(DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()));
                terminalAlarmInfoDTO.setAlarmInfo("于【" + DateTimeUtils.formatLocalDateTime(hisListPosition.get(0).getDate()) + "】线路偏移,开始报警");
                terminalAlarmInfoDTO.setAlarmType(AlarmTypeEnum.LINE_OFFSET);
                terminalAlarmInfoDTO.setCarNum(CarsAlarmConfigCacheSingle.getInstance().getMapCarTerminalId().get(terminalId));
                //线路偏移最大距离
                double maxDist = distList.stream().mapToDouble(s -> s.doubleValue()).summaryStatistics().getMax();
                terminalAlarmInfoDTO.setAlarmValue(maxDist);
                terminalAlarmInfoDTO.setAppKey(appKeyAlarmConfig.getAppKey());
                terminalAlarmInfoDTOS.add(terminalAlarmInfoDTO);
                Constant.terminalAlarmInfoCache.put(terminalId, terminalAlarmInfoDTOS);
                return terminalAlarmInfoDTO;
            }
        }
        return terminalAlarmInfoDTO;
    }


}
