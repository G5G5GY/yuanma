package com.yuanma.module.system.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuMetaVo {
    private String title;

    private String icon;

    private Boolean noCache;
}
