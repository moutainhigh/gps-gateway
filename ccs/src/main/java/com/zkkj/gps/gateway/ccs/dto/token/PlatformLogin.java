package com.zkkj.gps.gateway.ccs.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlatformLogin {
    @ApiModelProperty(value = "appkey", name = "appkey")
    private String appkey;
    @ApiModelProperty(value = "appsecret", name = "appsecret")
    private String appsecret;
    @ApiModelProperty(value = "设备终端编号，多个终端号使用逗号隔开（平台传*,平台下子公司传相应子公司下的终端号）", name = "terminals")
    private String terminals;
    @ApiModelProperty(name = "identity", value = "公司唯一标识", required = false)
    private String identity;

    public PlatformLogin(String appkey, String appsecret, String terminals, String identity) {
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.terminals = terminals;
        this.identity = identity;
    }

    public PlatformLogin() {
    }
}
