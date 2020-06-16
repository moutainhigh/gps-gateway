
package com.zkkj.gps.gateway.terminal.monitor.dto.ResultDto;


/**
 * 统一返回数据对象
 *
 * @param <T>
 */
public class ResultVo<T> {

    /**
     * 返回统一标识
     */
    private boolean success = true;
    /**
     * 返回信息
     */
    private String msg = "success";

    /**
     * 返回数据
     */
    private T data;

    /**
     * 统一返回失败方法
     *
     * @param msg 返回失败信息
     */
    public void resultFail(String msg) {
        this.success = false;
        this.msg = msg;
    }

    /**
     * 统一返回成功方法
     *
     * @param obj 返回数据
     */
    public void resultSuccess(T obj) {
        this.success = true;
        this.data = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
