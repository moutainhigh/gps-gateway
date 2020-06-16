package com.zkkj.gps.gateway.ccs.dto.baiDuDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * author : cyc
 * Date : 2020/4/15
 */

@Data
@ApiModel(value = "地区行政规划模型", description = "地区行政规划模型")
public class AddressComponent implements Serializable {

    @ApiModelProperty(name = "country", value = "国家")
    private String country;

    @ApiModelProperty(name = "countryCode", value = "国家编码")
    private int countryCode;

    @ApiModelProperty(name = "countryCodeIso", value = "国家英文缩写（三位）")
    private String countryCodeIso;

    @ApiModelProperty(name = "countryCodeIso2", value = "国家英文缩写（两位）")
    private String countryCodeIso2;

    @ApiModelProperty(name = "province", value = "省名")
    private String province;

    @ApiModelProperty(name = "city", value = "城市名")
    private String city;

    @ApiModelProperty(name = "cityLevel", value = "城市所在级别（仅国外有参考意义。国外行政区划与中国有差异，城市对应的层级不一定为『city』。country、province、city、district、town分别对应0-4级，若city_level=3，则district层级为该国家的city层级）")
    private int cityLevel;

    @ApiModelProperty(name = "district", value = "区县名")
    private String district;

    @ApiModelProperty(name = "town", value = "乡镇名")
    private String town;

    @ApiModelProperty(name = "townCode", value = "乡镇id")
    private String townCode;

    @ApiModelProperty(name = "street", value = "街道名（行政区划中的街道层级）")
    private String street;

    @ApiModelProperty(name = "streetNumber", value = "街道名（行政区划中的街道层级）")
    private String streetNumber;

    @ApiModelProperty(name = "adcode", value = "行政区划代码")
    private int adcode;

    @ApiModelProperty(name = "direction", value = "相对当前坐标点的方向，当有门牌号的时候返回数据")
    private String direction;

    @ApiModelProperty(name = "distance", value = "相对当前坐标点的距离，当有门牌号的时候返回数据")
    private String distance;


}
