package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.dbDto.SysLogDbDto;
import org.springframework.scheduling.annotation.Async;

/**
 * author : cyc
 * Date : 2020/3/18
 */
public interface SysLogService {

    @Async("taskExecutor")
    Object insertSysLog(SysLogDbDto sysLogDbDto);
}
