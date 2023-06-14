package com.yuanma.module.system.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDto {

    private Long id;

    private List<MenuDto> children;

    private Integer type;

    private String permission;

    private String title;

    private Integer menuSort;

    private String path;

    private String component;

    private Long pid;

    private Integer subCount;

    private Boolean iFrame;

    private Boolean cache;

    private Boolean hidden;

    private String componentName;

    private String icon;

    private String powerFlag;

    public Boolean getHasChildren() {
        return null!= subCount && subCount > 0;
    }

    public Boolean getLeaf() {
        return null!= subCount && subCount <= 0;
    }

    public String getLabel() {
        return title;
    }
}
