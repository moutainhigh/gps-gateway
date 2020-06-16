package com.zkkj.gps.gateway.ccs.dto.area;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: zkkjgs
 * @Description:
 * @Date: 2019-07-02 下午 7:10
 */
@Data
public class AreaEntity extends GroupAreaEntity {

    @ApiModelProperty(value = "用户登录标识（从Token中获取前端不用传入）", name = "appKey")
    private String appKey;

}