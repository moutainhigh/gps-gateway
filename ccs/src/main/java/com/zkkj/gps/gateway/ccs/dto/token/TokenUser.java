package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TokenUser {
    @ApiModelProperty(value = "设备车辆列表", name = "truckAndTerminalList")
    private List<TruckAndTerminal> truckAndTerminalList;
    @ApiModelProperty(value = "appkey", name = "appkey")
    private String appkey;
    @ApiModelProperty(value = "appsecret", name = "appsecret")
    private String appsecret;
    @ApiModelProperty(value = "时间戳", name = "timespan")
    private String timespan;
    @ApiModelProperty(value = "分组唯一识别码", name = "identity")
    private String identity;
    @ApiModelProperty(value = "所需车辆列表", name = "needTruckList")
    private List<String> needTerminalList;
    @ApiModelProperty(value = "公司名称", name = "corpName")
    private String corpName;
    public TokenUser() {

    }


    public TokenUser(List<TruckAndTerminal> truckAndTerminalList, String appkey, String appsecret, String timespan, String identity,List<String> needTerminalList) {
        this.truckAndTerminalList = truckAndTerminalList;
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.timespan = timespan;
        this.identity = identity;
        this.needTerminalList = needTerminalList;
    }
}
