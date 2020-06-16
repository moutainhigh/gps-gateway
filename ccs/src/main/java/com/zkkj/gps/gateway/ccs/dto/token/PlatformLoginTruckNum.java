package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlatformLoginTruckNum {
    @ApiModelProperty(value = "appkey", name = "appkey")
    private String appkey;
    @ApiModelProperty(value = "appsecret", name = "appsecret")
    private String appsecret;
    @ApiModelProperty(value = "车牌号，多个车牌号使用逗号隔开（平台传*,平台下子公司传相应子公司下的车牌号）", name = "truckNums")
    private String truckNums;
    @ApiModelProperty(name = "identity", value = "公司唯一标识", required = false)
    private String identity;

    public PlatformLoginTruckNum(String appkey, String appsecret, String truckNums, String identity) {
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.truckNums = truckNums;
        this.identity = identity;
    }

    public PlatformLoginTruckNum() {
    }
}
