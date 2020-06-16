package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.dbDto.SysLogDbDto;
import com.zkkj.gps.gateway.ccs.mappper.SysLogMapper;
import com.zkkj.gps.gateway.ccs.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author : cyc
 * Date : 2020/3/18
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 新增系统日志
     * @param sysLogDbDto
     * @return
     */
    @Override
    public Object insertSysLog(SysLogDbDto sysLogDbDto) {
        return sysLogMapper.insertSysLog(sysLogDbDto);
    }
}
