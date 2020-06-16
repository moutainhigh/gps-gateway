package com.zkkj.gps.gateway.terminal.monitor.task;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.HhdBusinessDto;
import com.zkkj.gps.gateway.terminal.monitor.mrsubscribe.gpsevent.*;
import com.zkkj.gps.gateway.terminal.monitor.utils.DesiccationPointUtil;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueList;
import com.zkkj.gps.gateway.terminal.monitor.utils.QueueListPositionUtil;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GpsMonitorSingle {

    private final Logger logger = LoggerFactory.getLogger(GpsMonitorSingle.class);

    private static GpsMonitorSingle singleInstance;

    ThreadPoolExecutor positionExecutor = new ThreadPoolExecutor(6, 12, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10000), new ThreadPoolExecutor.DiscardOldestPolicy());

    ThreadPoolExecutor webSocketExecutor = new ThreadPoolExecutor(7, 15, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10000), new ThreadPoolExecutor.DiscardOldestPolicy());

    static {
        singleInstance = new GpsMonitorSingle();
        singleInstance.init();
    }

    /**
     * 初始化方法
     */
    private void init() {
    }

    public static GpsMonitorSingle getInstance() {
        return singleInstance;
    }

    private int capacity = 10;
    //key:terminalId
    private Map<String, QueueList<BasicPositionDto>> mapQueueListPosition;

    //进出发货区域
    private int specialCapacity = 4;
    //只针对进出收发货区域
    private Map<String, QueueList<BasicPositionDto>> specialQueueListPosition;


    //缓存最新有效的终端对应的信息
    private Map<String, BaseGPSPositionDto> mapLatestAvailablePosition;

    public Map<String, BaseGPSPositionDto> getMapLatestAvailablePosition() {
        return mapLatestAvailablePosition;
    }

    private GpsMonitorSingle() {

        mapQueueListPosition = new HashMap<>();
        mapLatestAvailablePosition = new HashMap<>();
        specialQueueListPosition = new HashMap<>();
    }

    public void positionChange(String terminalId, BaseGPSPositionDto baseGPSPositionDto, String dispatchNo) {
        if (StringUtils.isBlank(terminalId)) {
            logger.error("终端terminalId为空");
            return;
        }
        //经纬度进行自身验证有效性验证
        if (baseGPSPositionDto == null) {
            logger.error("basicPositionDto" + baseGPSPositionDto);
            return;
        }

        ////对点位处理
        transformGpsPosition(terminalId, baseGPSPositionDto, dispatchNo);

        //发布未经处理过的经纬度事件通知，目前主要为持久化
        if (baseGPSPositionDto.getPoint() == null || !baseGPSPositionDto.getPoint().validate()) {
            return;
        }
        //原始点推送
        positionExecutor.execute(() -> EventBus.getDefault().post(new GpsOriginalInfoEvent(terminalId, baseGPSPositionDto)));
        //终端最新的点信息
        Boolean isValidate = validatePosition(terminalId, baseGPSPositionDto);
        //过滤点推送
        positionExecutor.execute(() -> EventBus.getDefault().post(new GpsFilterInfoEvent(terminalId, baseGPSPositionDto)));

        //websocket定位推送
        webSocketExecutor.execute(() -> EventBus.getDefault().post(new GpsWebSocketEvent(terminalId, baseGPSPositionDto)));
        //websocket任务定位推送
        webSocketExecutor.execute(() -> EventBus.getDefault().post(new GpsMonitorWebSocketEvent(terminalId, baseGPSPositionDto)));
        mapLatestAvailablePosition = updateTerminalLatestAvailablePosition(terminalId, baseGPSPositionDto);
        if (isValidate) {
            //将经纬度信息加入历史数据队列中
            QueueList<BasicPositionDto> hisListPosition = QueueListPositionUtil.updateTerminalHisPosition(baseGPSPositionDto, mapQueueListPosition, capacity);
            if (hisListPosition.isFull()) {
                EventBus.getDefault().post(new GpsListInfoEvent(terminalId, hisListPosition));
            }
            QueueList<BasicPositionDto> specialHisListPosition = QueueListPositionUtil.updateTerminalHisPosition(baseGPSPositionDto, specialQueueListPosition, specialCapacity);
            if (specialHisListPosition.isFull()) {
                EventBus.getDefault().post(new GpsSpecialListInfoEvent(terminalId, specialHisListPosition));
            }
        }

    }

    //如果传入当前点位中存在电子运单，则以当前的电子运单为最新，如果当前电子运单为空，则以之前存在的电子运单为准
    private void transformGpsPosition(String terminalId, BaseGPSPositionDto baseGPSPositionDto, String dispatchNo) {
        Object newEleDispatch = baseGPSPositionDto.getEleDispatch();
        if ((baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3) && newEleDispatch == null) {
            /**
             * 如果传过来的电子运单为空，则判断缓存中的电子运单和缓存中的运单中的运单编号是否一致
             * 如果不一致，则将电子运单置为空，如果一致直接覆盖
             */
            BaseGPSPositionDto oldBaseGPSPositionDto = mapLatestAvailablePosition.get(terminalId);
            if (oldBaseGPSPositionDto != null) {
                HhdBusinessDto oldEleDispatch = (HhdBusinessDto) oldBaseGPSPositionDto.getEleDispatch();
                if (oldEleDispatch != null && oldEleDispatch.getDisPatchNo().equals(dispatchNo)) {
                    baseGPSPositionDto.setEleDispatch(oldBaseGPSPositionDto.getEleDispatch());
                }
                if (oldEleDispatch != null && !oldEleDispatch.getDisPatchNo().equals(dispatchNo)) {
                    baseGPSPositionDto.setEleDispatch(null);
                }

            }
        }
        if ((baseGPSPositionDto.getFlag() == 1 || baseGPSPositionDto.getFlag() == 3) && newEleDispatch != null) {
            HhdBusinessDto hhdBusinessDto = (HhdBusinessDto) newEleDispatch;
            if (!dispatchNo.equals(hhdBusinessDto.getDisPatchNo())) {
                baseGPSPositionDto.setEleDispatch(null);
            }
        }
    }

    private Boolean validatePosition(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        BaseGPSPositionDto oldBasicPositionDto = mapLatestAvailablePosition.get(terminalId);
        if (oldBasicPositionDto != null) {
            return DesiccationPointUtil.validatePosition(baseGPSPositionDto.getPoint(), oldBasicPositionDto.getPoint());
        } else {
            return true;
        }
    }

    //更新缓存中最新有效的终端信息
    private Map<String, BaseGPSPositionDto> updateTerminalLatestAvailablePosition(String terminalId, BaseGPSPositionDto baseGPSPositionDto) {
        mapLatestAvailablePosition.put(terminalId, baseGPSPositionDto);
        return mapLatestAvailablePosition;
    }


}
