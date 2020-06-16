package com.zkkj.gps.gateway.ccs.dto.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * author : cyc
 * Date : 2020/3/5
 */

@Data
@ApiModel(value = "修改运单所属模型模型", description = "修改运单所属模型模型")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateDispatchIdentityInfoDto extends BaseUpdateDispatchInfoDto {

    @NotBlank(message = "传入的公司唯一标识不能为空")
    @ApiModelProperty(value = "公司唯一标识，纳税号，编码等", name = "identity")
    private String identity;

    @NotBlank(message = "修改人唯一标识不能为空")
    @ApiModelProperty(value = "修改人唯一标识", name = "updateBy")
    private String updateBy;

}
