package com.zkkj.gps.gateway.ccs.mappper;

import com.zkkj.gps.gateway.ccs.dto.dbDto.SysLogDbDto;
import org.mapstruct.Mapper;

/**
 * author : cyc
 * Date : 2020/3/18
 */

@Mapper
public interface SysLogMapper {

    /**
     * 新增系统日志
     *
     * @param sysLogDbDto
     * @return
     */
    int insertSysLog(SysLogDbDto sysLogDbDto);
}
