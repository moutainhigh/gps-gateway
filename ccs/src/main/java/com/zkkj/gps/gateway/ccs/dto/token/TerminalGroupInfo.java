package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TerminalGroupInfo {
    @ApiModelProperty(value = "群组设备列表", name = "terminalGroupList")
    private List<TerminalGroup> terminalGroupList;
}
