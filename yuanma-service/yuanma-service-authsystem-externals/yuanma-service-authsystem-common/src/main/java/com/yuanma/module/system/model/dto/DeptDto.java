package com.yuanma.module.system.model.dto;

//import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class DeptDto extends BaseDto {

    private Long id;

    private String name;

    private Boolean enabled;

    private Integer deptSort;

    //@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDto> children;

    private Long pid;

    private Integer subCount;

    private Boolean hasChildren;

    private Boolean leaf;

    private String label;

    public Boolean getHasChildren() {
        return null !=subCount && subCount > 0;
    }

    public Boolean getLeaf() {
        return null !=subCount && subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

}
