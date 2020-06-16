package com.zkkj.gps.gateway.ccs.dto.dbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/3/18
 */
@Data
@ApiModel(value = "系统日志模型", description = "系统日志模型")
public class SysLogDbDto implements Serializable {

    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @ApiModelProperty(name = "ip", value = "访问ip")
    private String ip;

    @ApiModelProperty(name = "url", value = "url访问路径")
    private String url;

    @ApiModelProperty(name = "methodType", value = "方法类型")
    private String methodType;

    @ApiModelProperty(name = "controller", value = "控制层名称")
    private String controller;

    @ApiModelProperty(name = "accessParameter", value = "访问参数")
    private String accessParameter;

    @ApiModelProperty(name = "accessResponse", value = "响应结果")
    private String accessResponse;

    @ApiModelProperty(name = "requestTime", value = "请求时间")
    private String requestTime;

    @ApiModelProperty(name = "responseTime", value = "响应时间")
    private String responseTime;

    @ApiModelProperty(name = "executionDuration", value = "耗时;毫秒")
    private Long executionDuration;

    @ApiModelProperty(name = "requestSource", value = "请求来源")
    private String requestSource;

    @ApiModelProperty(name = "requestSourceName", value = "请求来源名称")
    private String requestSourceName;
}