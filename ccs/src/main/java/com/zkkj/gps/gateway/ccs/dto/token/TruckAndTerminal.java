package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TruckAndTerminal {
    @ApiModelProperty(value = "车牌号", name = "truckNo")
    private String truckNo;
    @ApiModelProperty(value = "设备编号", name = "terminalNo")
    private String terminalNo;
    @ApiModelProperty(value = "所属群组", name = "belongGroup")
    private List<String> belongGroup;

    public TruckAndTerminal(String truckNo, String terminalNo) {
        this.truckNo = truckNo;
        this.terminalNo = terminalNo;
    }

    public TruckAndTerminal() {

    }
}