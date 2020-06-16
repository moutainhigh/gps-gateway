package com.zkkj.gps.gateway.terminal.monitor.utils;

import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BasicPositionDto;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * author : cyc
 * Date : 2019/9/10
 * 点位队列工具类
 */
public class QueueListPositionUtil {


    /**
     * 获取点位队列
     * @param baseGPSPositionDto
     * @param mapQueueListPosition
     * @param capacity
     * @return
     */
    public static  QueueList<BasicPositionDto> updateTerminalHisPosition(BaseGPSPositionDto baseGPSPositionDto, Map<String, QueueList<BasicPositionDto>> mapQueueListPosition, int capacity ) {
        String terminalId = baseGPSPositionDto.getTerminalId();
        if (mapQueueListPosition.containsKey(terminalId)) {
            QueueList<BasicPositionDto> list = mapQueueListPosition.get(terminalId);
            list.add(baseGPSPositionDto.getPoint());
            return list;
        } else {
            QueueList<BasicPositionDto> queueList = new QueueList<>(capacity);
            queueList.add(baseGPSPositionDto.getPoint());
            mapQueueListPosition.put(terminalId, queueList);
            return queueList;
        }
    }
}
