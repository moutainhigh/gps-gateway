package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("修改运单接口对象")
public class DispatchUpdateDto implements Serializable {

    @ApiModelProperty(value = "运单编号", name = "dispatchNo")
    private String dispatchNo;

    @ApiModelProperty(value = "运单状态信息", name = "status")
    private int status;

    @ApiModelProperty(value = "备注", name = "remake")
    private String remake;

    @ApiModelProperty(name = "terminalNo", value = "终端id")
    private String terminalNo;

    public DispatchUpdateDto(String dispatchNo, int status, String remake) {
        this.dispatchNo = dispatchNo;
        this.status = status;
        this.remake = remake;
    }

    public DispatchUpdateDto() {
    }
}
