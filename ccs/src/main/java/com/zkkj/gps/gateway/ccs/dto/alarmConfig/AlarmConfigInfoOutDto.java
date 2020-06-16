package com.zkkj.gps.gateway.ccs.dto.alarmConfig;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.AreaDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.LineStringDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmTypeEnum.AlarmTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * author : cyc
 * Date : 2019/8/26
 */
@Data
public class AlarmConfigInfoOutDto {

    //，由外部数据源传入，非主键
    @NotBlank(message = "报警配置id不能为空")
    @ApiModelProperty(name = "customAlarmConfigId", value = "报警配置id,长度<=40")
    private String customAlarmConfigId;

    @NotNull(message = "报警配置类型不能为空")
    @ApiModelProperty(name = "alarmTypeEnum", value = "报警类型,字符串枚举")
    private AlarmTypeEnum alarmTypeEnum;

    @ApiModelProperty(name = "configValue", value = "报警配置值")
    private double configValue;

    //@NotBlank(message = "报警配置所属公司名称不能为空")
    @ApiModelProperty(name = "corpName", value = "报警配置所属公司")
    private String corpName;

    @NotBlank(message = "公司唯一标识不能为空")
    @ApiModelProperty(name = "corpIdentity", value = "公司唯一标识，纳税号，编码等")
    private String corpIdentity;

    //报警区域
    @ApiModelProperty(name = "area", value = "报警区域")
    private AreaDto area;

    @ApiModelProperty(name = "startTime", value = "报警配置开始时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    @NotBlank(message = "开始时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    private String startTime;

    //@NotBlank(message = "结束时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(name = "startTime", value = "报警配置结束时间不能为空,格式:yyyy-MM-dd hh:mm:ss")
    private String endTime;

    @ApiModelProperty(name = "lineStrings", value = "线路报警，当报警类型为AlarmTypeEnum.LINE_OFFSET线路偏移时有效")
    private LineStringDto[] lineStrings;
}
