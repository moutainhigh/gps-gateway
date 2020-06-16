package com.zkkj.gps.gateway.ccs.dto.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * author : cyc
 * Date : 2018-09-20
 */
@Data
@Builder
@ApiModel(value = "导出excel数据模型")
public class ExcelDataDto implements Serializable {

    @ApiModelProperty(value = "表头名称", name = "titles")
    private List<String> titles;

    @ApiModelProperty(value = "每个单元格是一个对象，一条数据是很多单元格的集合", name = "rowsData")
    private List<List<Object>> rowsData;

    @ApiModelProperty(value = "表名称", name = "sheetName")
    private String sheetName;
}
