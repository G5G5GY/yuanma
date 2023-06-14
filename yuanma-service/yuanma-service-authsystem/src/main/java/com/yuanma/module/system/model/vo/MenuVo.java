package com.yuanma.module.system.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuVo {

    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuVo> children;

}
