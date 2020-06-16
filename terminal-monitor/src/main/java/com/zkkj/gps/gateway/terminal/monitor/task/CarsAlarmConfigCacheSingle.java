package com.zkkj.gps.gateway.terminal.monitor.task;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarNumTerminalIdDto;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarsAlarmConfigCacheSingle {
    private static CarsAlarmConfigCacheSingle singleInstance;

    //key:string标识terminalId，缓存对应每个终端的报警配置
    private Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig;
    //key:terminalId,value:carNum,缓存设备id及车牌号对应关系
    private Map<String, String> mapCarTerminalId;

    public Map<String, List<AppKeyAlarmConfigDto>> getMapAlarmConfig() {
        return mapAlarmConfig;
    }

    public Map<String, String> getMapCarTerminalId() {
        return mapCarTerminalId;
    }

    static {
        singleInstance = new CarsAlarmConfigCacheSingle();
        singleInstance.init();
    }

    public static CarsAlarmConfigCacheSingle getInstance() {
        return singleInstance;
    }

    private CarsAlarmConfigCacheSingle() {
        mapAlarmConfig = new HashMap<>();
        mapCarTerminalId = new HashMap<>();
    }

    /**
     * 初始化操作
     */
    private void init() {

    }

    /**
     * 更新第三方配置的终端报警配置信息
     * 按照appkey、车辆设备信息、以及报警配置进行报警分类重组去重，构建 carAlarmConfigs,需要给carAlarmConfigs赋值appkey
     *
     * @param appKey          第三方应用key
     * @param dispatchNo      运单编号
     * @param carAlarmConfigs 终端报警配置信息
     */
    public synchronized void updateAlarmConfig(String appKey, String dispatchNo, List<CarAlarmConfigDto> carAlarmConfigs) {
        if (CollectionUtils.isNotEmpty(carAlarmConfigs)) {
            for (int i = 0; i < carAlarmConfigs.size(); i++) {
                CarAlarmConfigDto carAlarmConfigDTO = carAlarmConfigs.get(i);
                //获取配置信息
                List<AlarmConfigDto> alarmConfigs = carAlarmConfigDTO.getAlarmConfig();
                //获取车辆信息
                List<CarNumTerminalIdDto> cars = carAlarmConfigDTO.getCars();
                if (CollectionUtils.isNotEmpty(cars) && CollectionUtils.isNotEmpty(alarmConfigs)) {
                    for (int j = 0; j < cars.size(); j++) {
                        CarNumTerminalIdDto carNumTerminalIdDTO = cars.get(j);
                        String terminalId = carNumTerminalIdDTO.getTerminalId();
                        String carNum = carNumTerminalIdDTO.getCarNum();
                        //根据终端id获取缓存中报警信息
                        List<AppKeyAlarmConfigDto> appKeyAlarmConfigDTOS = mapAlarmConfig.get(terminalId);
                        for (int k = 0; k < alarmConfigs.size(); k++) {
                            AlarmConfigDto alarmConfigDTO = alarmConfigs.get(k);
                            //2019-06-20 添加任务单编号
                            alarmConfigDTO.setDispatchNo(dispatchNo);
                            AppKeyAlarmConfigDto appKeyAlarmConfig = new AppKeyAlarmConfigDto(appKey, alarmConfigDTO);
                            //如果报警信息为空
                            if (CollectionUtils.isNotEmpty(appKeyAlarmConfigDTOS)) {
                                if (appKeyAlarmConfigDTOS.contains(appKeyAlarmConfig)) {
                                    appKeyAlarmConfigDTOS.remove(appKeyAlarmConfig);
                                }
                                appKeyAlarmConfigDTOS.add(appKeyAlarmConfig);
                            } else {
                                appKeyAlarmConfigDTOS = Lists.newArrayList();
                                appKeyAlarmConfigDTOS.add(appKeyAlarmConfig);
                            }
                            mapAlarmConfig.put(terminalId, appKeyAlarmConfigDTOS);
                            mapCarTerminalId.put(terminalId, carNum);
                        }
                    }
                }
            }
        }
    }
}
