package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TerminalGroup {
    @ApiModelProperty(value = "群组名称", name = "groupName")
    private String groupName;
    @ApiModelProperty(value = "设备编号列表", name = "terminalNo")
    private List<String> terminalNo;
}
