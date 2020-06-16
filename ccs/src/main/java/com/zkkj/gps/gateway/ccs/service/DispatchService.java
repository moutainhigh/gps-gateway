package com.zkkj.gps.gateway.ccs.service;

import com.zkkj.gps.gateway.ccs.dto.dispatch.*;

import java.util.List;

public interface DispatchService {
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
    List<DispatchAddDto> getDispatchByTerminalId(String terminalId);

    /**
     * 根据运单号修改终端设备编号
     *
     * @param updateDispatchInfoDto
     */
    void updateDispatchInfoByDispatchNo(UpdateDispatchInfoDto updateDispatchInfoDto) throws Exception;

    /**
     * 手动取消或者结束运单
     *
     * @param baseUpdateDispatchInfoDto
     */
    void cancelDispatchInfo(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) throws Exception;

    /**
     * 将运单信息添加到缓存
     *
     * @param baseUpdateDispatchInfoDto
     */
    void addDispatchInfoCache(BaseUpdateDispatchInfoDto baseUpdateDispatchInfoDto) throws Exception;

    /**
     * 手动清除运单(针对运单没有正常结束)接口
     *
     * @param baseUpdateDispatchInfoDto
     */
    void clearDispatchInfo(List<BaseUpdateDispatchInfoDto> baseUpdateDispatchInfoDto) throws Exception;

    /**
     * 添加运单接口
     *
     * @param dispatchInfoDto
     */
    void addMonitorConfigInfo(DispatchInfoDto dispatchInfoDto) throws Exception;

    /**
     * 更新任务监控所属关系
     *
     * @param updateDispatchIdentityInfoDto
     */
    void updateMonitorAffiliation(UpdateDispatchIdentityInfoDto updateDispatchIdentityInfoDto) throws Exception;
}
