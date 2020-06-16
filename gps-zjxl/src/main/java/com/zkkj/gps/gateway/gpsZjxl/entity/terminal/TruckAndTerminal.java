package com.zkkj.gps.gateway.gpsZjxl.entity.terminal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TruckAndTerminal implements Serializable {

    @ApiModelProperty(name = "truckNo", value = "车牌号")
    private String truckNo;

    @ApiModelProperty(name = "terminalNo", value = "设备编号")
    private String terminalNo;

    @ApiModelProperty(name = "belongGroup", value = "所属群组")
    private List<String> belongGroup;

    public TruckAndTerminal(String truckNo, String terminalNo) {
        this.truckNo = truckNo;
        this.terminalNo = terminalNo;
    }

    public TruckAndTerminal() {
    }
}