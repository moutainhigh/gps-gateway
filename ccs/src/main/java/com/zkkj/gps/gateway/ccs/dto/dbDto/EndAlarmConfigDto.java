package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019-05-21
 */

@Data
@ApiModel(value = "更新报警配置模型", description = "更新报警配置模型")
public class EndAlarmConfigDto implements Serializable {

    @NotBlank(message = "终端编号不能为空")
    @ApiModelProperty(name = "terminalId", value = "终端编号")
    private String terminalId;

    @NotBlank(message = "appKey不能为空")
    @ApiModelProperty(name = "appKey", value = "第三方应用key")
    private String appKey;

    @NotBlank(message = "需要修改的报警配置id不能为空")
    @ApiModelProperty(name = "customConfigId", value = "外部传来的报警配置id")
    private String customConfigId;

    @NotBlank(message = "结束时间不能为空,格式yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(name = "endTime", value = "结束时间,格式yyyy-MM-dd hh:mm:ss")
    private String endTime;

    @ApiModelProperty(name = "disPatchNo", value = "运单编号,不存在运单的时候可以不用传该字段或者该字段传空")
    private String disPatchNo;

    public EndAlarmConfigDto(String terminalId, String appKey, String endTime) {
        this.terminalId = terminalId;
        this.appKey = appKey;
        this.endTime = endTime;
    }

    public EndAlarmConfigDto() {
    }
}
