package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TerminalGroupDto {
    @ApiModelProperty(value = "群组设备集合", name = "groupTerminalList")
    private Map<String,List<TerminalTruckOnly>> groupTerminalList;

    @ApiModelProperty(value = "未分组设备信息", name = "terminalInfo")
    private List<TerminalTruckOnly> terminalInfo;
    @ApiModelProperty(value = "公司名称", name = "corpName")
    private String corpName;
}
