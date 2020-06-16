package com.zkkj.gps.gateway.ccs.dto.baiDuDto;

import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2019/11/13
 */

@Data
public class BaiDuDto implements Serializable {

    private Integer status;

    private Location location;

    private Integer precise;

    private Integer confidence;

    private Integer comprehension;

    private Integer level;

}
