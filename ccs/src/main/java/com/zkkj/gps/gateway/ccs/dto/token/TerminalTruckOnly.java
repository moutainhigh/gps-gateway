package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TerminalTruckOnly {
    @ApiModelProperty(value = "车牌号", name = "truckNo")
    private String truckNo;
    @ApiModelProperty(value = "设备编号", name = "terminalNo")
    private String terminalNo;

    public TerminalTruckOnly(String truckNo,String terminalNo)
    {
        this.truckNo=truckNo;
        this.terminalNo=terminalNo;
    }

    public TerminalTruckOnly()
    {

    }
}
