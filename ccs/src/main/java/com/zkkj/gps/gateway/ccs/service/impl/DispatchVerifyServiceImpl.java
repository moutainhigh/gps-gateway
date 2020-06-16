package com.zkkj.gps.gateway.ccs.service.impl;

import com.zkkj.gps.gateway.ccs.dto.gpsDto.InAreaDto;
import com.zkkj.gps.gateway.ccs.exception.ParamException;
import com.zkkj.gps.gateway.ccs.service.DispatchVerifyService;
import com.zkkj.gps.gateway.ccs.service.IGenerator;
import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.gpsDto.BaseGPSPositionDto;
import com.zkkj.gps.gateway.terminal.monitor.task.GpsMonitorSingle;
import com.zkkj.gps.gateway.terminal.monitor.utils.GPSPositionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.awt.geom.Point2D;

/**
 * 车辆派车信息验证业务实现
 * @author suibozhuliu
 */
@Service
public class DispatchVerifyServiceImpl implements DispatchVerifyService{

    @Value("${out.line.time}")
    private int outLineTime;

    @Autowired
    private IGenerator iGenerator;

    @Override
    public boolean terminalOnLineVerify(String terminalNo) {
        BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalNo);
        if (!ObjectUtils.isEmpty(baseGPSPositionDto) && !ObjectUtils.isEmpty(baseGPSPositionDto.getPoint())){
            boolean dateFlag = DateTimeUtils.durationMinutes(baseGPSPositionDto.getPoint().getDate(), DateTimeUtils.getCurrentLocalDateTime()) > outLineTime;
            if (!dateFlag) {
                return true;
            }
        } else {
            throw new ParamException("未查到对应的位置信息！");
        }
        return false;
    }

    @Override
    public boolean terminalInAreaVerify(String terminalNo, InAreaDto inAreaDto) {
        BaseGPSPositionDto baseGPSPositionDto = GpsMonitorSingle.getInstance().getMapLatestAvailablePosition().get(terminalNo);
        if (!ObjectUtils.isEmpty(baseGPSPositionDto) && !ObjectUtils.isEmpty(baseGPSPositionDto.getPoint())){
            AreaDto area = iGenerator.convert(inAreaDto, AreaDto.class);
            if (!ObjectUtils.isEmpty(area)){
                boolean isInAreaFlag = GPSPositionUtil.checkSingleInArea(new Point2D.Double(baseGPSPositionDto.getPoint().getLongitude(),
                        baseGPSPositionDto.getPoint().getLatitude()), area);
                if (isInAreaFlag){//在指定区域
                    return true;
                }
            } else {
                throw new ParamException("区域信息异常！");
            }
        } else {
            throw new ParamException("未查到对应的位置信息！");
        }
        return false;
    }
}
