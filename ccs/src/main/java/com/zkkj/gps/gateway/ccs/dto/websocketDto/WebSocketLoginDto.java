package com.zkkj.gps.gateway.ccs.dto.websocketDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/5/10
 */

@Data
@ApiModel(value = "webSocket登录参数模型")
public class WebSocketLoginDto implements Serializable {

    @NotBlank(message = "设备编号不能为空")
    @ApiModelProperty(name = "terminalIds", value = "设备编号,所有*，多个设备编号用,分开，例如:4545,56545")
    private String terminalIds;

    @NotBlank(message = "所属公司编号不能为空")
    @ApiModelProperty(name = "identity", value = "归属标识，例如:zk0001")
    private String identity;




}
