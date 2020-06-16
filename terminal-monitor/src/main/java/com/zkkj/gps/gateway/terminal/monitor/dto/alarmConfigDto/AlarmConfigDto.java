package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import com.zkkj.gps.gateway.common.utils.DateTimeUtils;
import com.zkkj.gps.gateway.terminal.monitor.dto.ResultDto.ResultVo;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.GraphTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class AlarmConfigDto implements Serializable {


    /**
     * 由外部数据源传入，非主键
     */
    @NotBlank(message = "报警配置id不能为空")
    private String customAlarmConfigId;

    /**
     * 报警类型
     */
    @NotNull(message = "报警配置类型不能为空")
    private AlarmTypeEnum alarmTypeEnum;

    /**
     * 报警配置值
     */
    private double configValue;

    /**
     * 报警配置所属公司
     */
    //@NotBlank(message = "报警配置所属公司名称不能为空")
    private String corpName;

    /**
     * 公司唯一标识，纳税号，编码等
     */
    @NotBlank(message = "公司唯一标识不能为空")
    private String corpIdentity;
    /**
     * 报警区域
     */
    private AreaDto area;

    /**
     * 运单编号
     */
    private String dispatchNo;


    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    private String startTime;

    /**
     * 结束时间
     */
    //@NotBlank(message = "结束时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    private String endTime;

    /**
     * 线路报警，当报警类型为AlarmTypeEnum.LINE_OFFSET线路偏移时有效
     */
    private LineStringDto[] lineStrings;

    /**
     * 是否收发货区域为报警产生0不是收发货区域，1是收发货区域
     */
    private int isDeliveryArea;


    public ResultVo<String> validate() {
        ResultVo<String> result = new ResultVo<>();
        try {
            LocalDateTime startDateTime = DateTimeUtils.parseLocalDateTime(startTime);
            LocalDateTime endDateTime = DateTimeUtils.parseLocalDateTime(endTime);
            boolean flag = DateTimeUtils.durationMillis(startDateTime, endDateTime) >= 0 ? true : false;
            //TODO 验证当前模型数据正确性,如报警类型为线路偏移时，线路集合不能为空，为进出区域时，区域信息不能为空
            if (flag) {
                if (alarmTypeEnum == AlarmTypeEnum.VIOLATION_AREA) {
                    if (area == null) {
                        result.resultFail("当报警类型为违规区域，则区域不能为空");
                        return result;
                    }
                    GraphTypeEnum graphTypeEnum = area.getGraphTypeEnum();
                    if (graphTypeEnum == GraphTypeEnum.POLYGON) {
                        AreaPointsDto[] areaPoints = area.getAreaPoints();
                        if (areaPoints == null || areaPoints.length <= 0) {
                            result.resultFail("当报警类型为违规区域，则区域点不能为空");
                            return result;
                        }
                    }
                }
                if (alarmTypeEnum == AlarmTypeEnum.LINE_OFFSET) {
                    if (lineStrings == null || lineStrings.length <= 0) {
                        result.resultFail("当报警类型为路线偏移，则路线不能为空");
                        return result;
                    }
                }
                return result;
            }
            result.resultFail("报警配置开始时间应小于等于结束时间");
        } catch (Exception e) {
            result.resultFail("时间转化异常，标准格式:yyyy-MM-dd HH:mm:ss");
        }
        return result;
    }


}
