package com.zkkj.gps.gateway.ccs.dto.baiDuDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/11/13
 */

@Data
@ApiModel(value = "百度模型")
public class BaiDuResult<T> implements Serializable {

    @ApiModelProperty(name = "status", value = "返回结果状态值， 成功返回0")
    private int status;

    @ApiModelProperty(name = "result", value = "返回結果")
    private T result;

}
