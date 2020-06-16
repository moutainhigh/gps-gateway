package com.zkkj.gps.gateway.terminal.monitor.dto.alarmConfigDto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/8/29
 */

@Data
public class CarNumTerminalIdDto implements Serializable {

    @NotBlank(message = "车牌号不能为空")
    private  String carNum;

    @NotBlank(message = "终端编号不能为空")
    private  String terminalId;

    public CarNumTerminalIdDto() {
    }

    public CarNumTerminalIdDto(String carNum, String terminalId) {

        this.carNum = carNum;
        this.terminalId = terminalId;
    }
}
