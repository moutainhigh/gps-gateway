package com.zkkj.gps.gateway.terminal.monitor.task;

import com.google.common.collect.Lists;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AppKeyAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarAlarmConfigDto;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * CarsAlarmConfigCacheSingle Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>四月 19, 2019</pre>
 */
public class CarsAlarmConfigCacheSingleTest {

    //appKey
    private String appKey = "1";

    //初始化数据
    List<CarAlarmConfigDto> carAlarmConfigList = Lists.newArrayList();
    List<CarAlarmConfigDto> carAlarmConfigList1 = Lists.newArrayList();

    Map<String, List<AppKeyAlarmConfigDto>> mapAlarmConfig;

    Map<String, String> mapCarTerminalId;

    /**
     * Method: updateAlarmConfig(String appKey, List<CarAlarmConfigDto> carAlarmConfigs)
     */
    @Test
    public void testUpdateAlarmConfig() throws Exception {
        //TODO: Test 更新报警配置，需要进行多线程测试
        //初始化数据
//        initData();
//        CarsAlarmConfigCacheSingle carsAlarmConfigCacheSingle = new CarsAlarmConfigCacheSingle();
//        carsAlarmConfigCacheSingle.updateAlarmConfig("1", carAlarmConfigList);
//        mapAlarmConfig = carsAlarmConfigCacheSingle.getMapAlarmConfig();
//        initData1();
//        carsAlarmConfigCacheSingle.updateAlarmConfig("1", carAlarmConfigList1);
//        mapAlarmConfig = carsAlarmConfigCacheSingle.getMapAlarmConfig();
//        mapCarTerminalId = carsAlarmConfigCacheSingle.getMapCarTerminalId();
//        //testGetMapCarTerminalId();
//        System.out.println(mapCarTerminalId);

    }

    private void initData() {

//        List<AlarmConfigDto> alarmConfigs = Lists.newArrayList();
//        AlarmConfigDto alarmConfigDTO = new AlarmConfigDto("1", AlarmTypeEnum.OFF_LINE, "河北邯郸");
//        AlarmConfigDto alarmConfigDTO1 = new AlarmConfigDto("2", AlarmTypeEnum.OFF_LINE, "河北邢台");
//        AlarmConfigDto alarmConfigDTO2 = new AlarmConfigDto("3", AlarmTypeEnum.OFF_LINE, "河北唐山");
//        alarmConfigs.add(alarmConfigDTO);
//        alarmConfigs.add(alarmConfigDTO1);
//        alarmConfigs.add(alarmConfigDTO2);
//
//        List<CarNumTerminalIdDto> cars = Lists.newArrayList();
//        CarNumTerminalIdDto carNumTerminalIdDTO = new CarNumTerminalIdDto("冀aaaa", "冀aaaa");
//        CarNumTerminalIdDto carNumTerminalIdDTO1 = new CarNumTerminalIdDto("冀bbbb", "冀bbbb");
//        cars.add(carNumTerminalIdDTO);
//        cars.add(carNumTerminalIdDTO1);
//
//        CarAlarmConfigDto carAlarmConfigDTO = new CarAlarmConfigDto(cars, alarmConfigs);
//
//
//        carAlarmConfigList.add(carAlarmConfigDTO);
    }

    private void initData1() {
//        List<AlarmConfigDto> alarmConfigs = Lists.newArrayList();
//        AlarmConfigDto alarmConfigDTO = new AlarmConfigDto("1", AlarmTypeEnum.OFF_LINE, "河北邯郸");
//        AlarmConfigDto alarmConfigDTO1 = new AlarmConfigDto("2", AlarmTypeEnum.OFF_LINE, "河北邢台");
//        AlarmConfigDto alarmConfigDTO2 = new AlarmConfigDto("3", AlarmTypeEnum.OFF_LINE, "河北唐山");
//        AlarmConfigDto alarmConfigDTO3 = new AlarmConfigDto("4", AlarmTypeEnum.OFF_LINE, "河北唐山");
//        alarmConfigs.add(alarmConfigDTO);
//        alarmConfigs.add(alarmConfigDTO1);
//        alarmConfigs.add(alarmConfigDTO2);
//        alarmConfigs.add(alarmConfigDTO3);
//
//        List<CarNumTerminalIdDto> cars = Lists.newArrayList();
//        CarNumTerminalIdDto carNumTerminalIdDTO = new CarNumTerminalIdDto("冀aaaa", "冀aaaa");
//        CarNumTerminalIdDto carNumTerminalIdDTO1 = new CarNumTerminalIdDto("冀bbbb", "冀bbbb");
//        cars.add(carNumTerminalIdDTO);
//        cars.add(carNumTerminalIdDTO1);
//
//        CarAlarmConfigDto carAlarmConfigDTO = new CarAlarmConfigDto(cars, alarmConfigs);
//
//
//        carAlarmConfigList1.add(carAlarmConfigDTO);
    }

    /**
     * Method: getMapAlarmConfig()
     */
    @Test
    public void testGetMapAlarmConfig() throws Exception {
        //TODO: Test，获取值不能为空
        if (!(mapAlarmConfig != null && mapAlarmConfig.size() > 0)) {
            //throw new RuntimeException("终端对应报警配置为空");
        }
    }

    /**
     * Method: getMapCarTerminalId()
     */
    @Test
    public void testGetMapCarTerminalId() throws Exception {
        //TODO: Test，获取值不能为空
        if (!(mapCarTerminalId != null && mapCarTerminalId.size() > 0)) {
            //throw new RuntimeException("设备编号及车牌号关系为空");
        }
    }

}
