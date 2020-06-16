package com.zkkj.gps.gateway.ccs.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author : cyc
 * Date : 2019/11/20
 */

@Data
@ApiModel(value = "统一返回类型(非分页的)", description = "统一返回类型(非分页的)")
public class BaseResultVo<T> {

    @ApiModelProperty(value = "请求成功失败标识 true 成功 false 失败", name = "success", example = "true")
    private boolean success = true;

    @ApiModelProperty(value = "返回信息 当success错误时 返回错误信息", name = "msg", example = "请输入密码！")
    private String msg = "success";

    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    public void resultFail(String msg) {
        this.success = false;
        this.msg = msg;
    }

    public void resultSuccess(T obj) {
        this.success = true;
        this.data = obj;
    }

    public BaseResultVo() {
    }
}
