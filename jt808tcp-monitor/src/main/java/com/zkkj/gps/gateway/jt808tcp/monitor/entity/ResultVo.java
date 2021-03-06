package com.zkkj.gps.gateway.jt808tcp.monitor.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一响应基类
 * @Auther: suibozhuliu
 * @Description:
 * @Date: 2019-09-11 下午 5:28
 */
@Data
@ApiModel(value = "统一返回类型", description = "统一返回类型")
public class ResultVo<T> {
    /**
     * 返回统一值
     */
    @ApiModelProperty(value = "请求成功失败标识 true 成功 false 失败", name = "success")
    private boolean success = true;
    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息 当success错误时 返回错误信息", name = "msg")
    private String msg = "success";

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    public ResultVo() {
    }

    /**
     * 失败响应数据设置
     *
     * @param msg
     */
    public void resultFail(String msg) {
        this.success = false;
        this.msg = msg;
    }

    /**
     * 成功响应数据设置
     *
     * @param obj
     */
    public void resultSuccess(T obj, String msg) {
        this.success = true;
        this.data = obj;
        this.msg = msg;
    }
}
