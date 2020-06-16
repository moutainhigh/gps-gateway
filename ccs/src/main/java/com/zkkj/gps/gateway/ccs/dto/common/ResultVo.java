package com.zkkj.gps.gateway.ccs.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "统一返回类型", description = "统一返回类型")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo<T> {

    @ApiModelProperty(value = "请求成功失败标识 true 成功 false 失败", name = "success", example = "true")
    private boolean success = true;

    @ApiModelProperty(value = "返回信息 当success错误时 返回错误信息", name = "msg", example = "请输入密码！")
    private String msg = "操作成功";

    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    @ApiModelProperty(value = "分页总数 默认0", name = "total", example = "100")
    private int total = 0;

    public void resultFail(String msg) {
        this.success = false;
        this.msg = msg;
    }

    public void resultSuccess(T obj) {
        this.success = true;
        this.data = obj;
    }

}
