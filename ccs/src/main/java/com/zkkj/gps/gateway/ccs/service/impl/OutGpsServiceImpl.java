package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchInfoDto;
import com.zkkj.gps.gateway.ccs.dto.dispatch.DispatchUpdateDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.GpsBusinessDto;
import com.zkkj.gps.gateway.ccs.dto.gpsDto.OutGpsBusinessDto;
import com.zkkj.gps.gateway.ccs.monitorconfig.MonitorConfigCache;
import com.zkkj.gps.gateway.ccs.service.DispatchService;
import com.zkkj.gps.gateway.ccs.service.OutGpsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * author : cyc
 * Date : 2019-07-05
 */
@Service
public class OutGpsServiceImpl implements OutGpsService {

    @Autowired
    private DispatchService dispatchService;

    /**
     * 修改电子运单状态并且推送运单状态变更事件
     *
     * @param outGpsBusinessDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setBusinessStatus(OutGpsBusinessDto outGpsBusinessDto) throws Exception {
        GpsBusinessDto gpsBusinessDto = outGpsBusinessDto.getGpsBusinessDto();
        String status = gpsBusinessDto.getStatus();
        if (StringUtils.isNotBlank(status)) {
            switch (status) {
                case "0":
                    status = "10";
                    break;
                case "1":
                    status = "20";
                    break;
                case "2":
                    status = "70";
                    break;
                case "3":
                    status = "200";
                    break;
                case "4":
                    status = "210";
                    break;
                case "5":
                    status = "240";
                    break;
                case "6":
                    status = "260";
                    break;
                case "7":
                    status = "270";
                    break;
            }
            //新增或者派车
            String disPatchNo = gpsBusinessDto.getDisPatchNo();
            Map<String, DispatchInfoDto> dispatchInfoDtoMap = MonitorConfigCache.getInstance().getDispatchInfoDtoMap();
            DispatchInfoDto dispatchInfoDto = dispatchInfoDtoMap.get(outGpsBusinessDto.getTerminalId());
            if (StringUtils.isNotBlank(disPatchNo) && dispatchInfoDto != null && disPatchNo.equals(dispatchInfoDto.getDispatchNo())) {
                dispatchService.updateDispatchInfo(new DispatchUpdateDto(disPatchNo, Integer.valueOf(status), ""));
                if ("270".equals(status)) {
                    //dispatchInfoDtoMap.remove(outGpsBusinessDto.getTerminalId());
                    MonitorConfigCache.removeMonitorConfigCache(outGpsBusinessDto.getTerminalId());
                }
            }
        }
    }

}
