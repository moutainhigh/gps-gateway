package com.zkkj.gps.gateway.terminal.monitor.constant;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmInfoDto.TerminalAlarmInfoDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author : cyc
 * Date : 2019-06-10
 */
public class Constant {

    /**
     * 报警信息缓存常量
     */
    public static Map<String, List<TerminalAlarmInfoDto>> terminalAlarmInfoCache = new ConcurrentHashMap<>();
}
