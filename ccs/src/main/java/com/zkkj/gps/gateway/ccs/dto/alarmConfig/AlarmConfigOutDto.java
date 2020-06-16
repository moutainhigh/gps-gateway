package com.zkkj.gps.gateway.ccs.dto.alarmConfig;

import com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto.CarNumTerminalIdDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AlarmConfigOutDto {

    @NotEmpty(message = "报警配置信息不能为空")
    @Valid
    @ApiModelProperty(name = "alarmConfig", value = "报警配置详细信息")
    private List<AlarmConfigInfoOutDto> alarmConfig;

    @NotBlank(message = "appkey不能为空")
    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;

    @NotEmpty(message = "车辆终端信息不能为空")
    @Valid
    @ApiModelProperty(name = "cars", value = "终端设备id和车牌号绑定关系集合")
    private List<CarNumTerminalIdDto> cars;

}
