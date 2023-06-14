package com.yuanma.module.system.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zsheng
 * @Date 2022/3/21 14:20
 * @Version 1.0
 * @Describe:
 */
@Data
public class DictQuery {
    @ApiModelProperty("字典类型")
    private String dictType;
    @ApiModelProperty("字典名称")
    private String dictName;
    @ApiModelProperty("字典标识")
    private String dictLogo;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("页数")
    private Integer page = 0;
    @ApiModelProperty("每页多少条")
    private Integer size = 10;
}
