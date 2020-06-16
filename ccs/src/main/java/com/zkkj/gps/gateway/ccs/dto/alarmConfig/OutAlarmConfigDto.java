package com.zkkj.gps.gateway.ccs.dto.alarmConfig;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarAlarmConfigDto;
import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarNumTerminalIdDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class OutAlarmConfigDto {

    @NotBlank(message = "appkey不能为空")
    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;

    @NotEmpty(message = "终端报警配置信息不能为空")
    @Valid
    @ApiModelProperty(name = "carAlarmConfigs", value = "终端报警配置信息")
    private List<CarAlarmConfigDto> carAlarmConfigs;

    @ApiModelProperty(name = "dispatchNo", value = "订单编号")
    private String dispatchNo;
}
