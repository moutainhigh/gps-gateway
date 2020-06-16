package com.zkkj.gps.gateway.ccs.dto.route;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-07-02 下午 7:49
 */
@Data
public class RouteEntity extends GroupRouteEntity {

    @ApiModelProperty(value = "用户登录标识（从Token中获取前端不用传入）", name = "appKey")
    private String appKey;

}