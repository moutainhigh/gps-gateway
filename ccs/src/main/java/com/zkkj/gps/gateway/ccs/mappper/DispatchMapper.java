package com.zkkj.gps.gateway.ccs.mappper;


import com.zkkj.gps.gateway.ccs.dto.dispatch.BaseUpdateDispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchAddDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.UpdateDispatchIdentityInfoDto;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DispatchMapper {

    /**
     * 新增对应的运单信息
     *
     * @param dispatchAddDto
     * @return
     */
    int addDispatchInfo(DispatchAddDto dispatchAddDto);


    /**
     * 修改运单状态信息
     *
     * @param dispatchUpdateDto
     * @return
     */
    int updateDispatchInfo(DispatchUpdateDto dispatchUpdateDto);


    /**
     * 初始化加载从数据化加载到缓存中
     *
     * @return
     */
    List<DispatchAddDto> getDispatchCacheListInfo();


    /**
     * 获取设备编号对应的未完成的运单
     *
     * @param terminalId
     * @return
     */
    List<DispatchAddDto> getDispatchByTerminalId(@Param("terminalId") String terminalId);

    /**
     * 根据终端编号和运单编号获取运单信息
     *
     * @param baseUpdateDispatchInfoDto
     * @return
     */
    DispatchAddDto getDispatchInfo(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto);

    /**
     * 更新任务监控所属关系
     *
     * @param updateDispatchIdentityInfoDto
     * @return
     */
    int updateMonitorAffiliation(UpdateDispatchIdentityInfoDto updateDispatchIdentityInfoDto);
}
