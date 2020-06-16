package com.zkkj.gps.gateway.ccs.dto.dispatch;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.LineStringDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报警配置用户传入对象")
public class MonitorConfigDto {
    @ApiModelProperty(value = "由外部数据源传入，非主键", name = "id")
    private String customConfigId;
    @ApiModelProperty(value = "报警类型", name = "alarmTypeEnum")
    private AlarmTypeEnum alarmTypeEnum;
    @ApiModelProperty(value = "报警配置值", name = "configValue")
    private double configValue;
    @ApiModelProperty(value = "报警区域", name = "area")
    private AreaDto area;
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;
    @ApiModelProperty(value = "线路报警，当报警类型为AlarmTypeEnum.LINE_OFFSET线路偏移时有效", name = "lineStrings")
    private LineStringDto[] lineStrings;
    @ApiModelProperty(value = "所属公司名称", name = "corpName")
    private String corpName;
}
