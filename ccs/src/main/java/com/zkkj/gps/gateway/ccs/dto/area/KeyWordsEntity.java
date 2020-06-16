package com.zkkj.gps.gateway.ccs.dto.area;

import io.swagger.annotations.ApiModel;
        import io.swagger.annotations.ApiModelProperty;
        import lombok.Data;

@Data
@ApiModel("关键字搜索实体")
public class KeyWordsEntity {
    @ApiModelProperty(value = "每一页多少数据", name = "pageSize")
    private int pageSize;
    @ApiModelProperty(value = "页码", name = "currentPage")
    private int currentPage;
    @ApiModelProperty(value = "关键字", name = "keyWords")
    private String keyWords;
    @ApiModelProperty(value = "分组唯一识别码（平台传*，平台下子用户传相应identity）", name = "identity")
    public String identity;
}