package com.yuanma.module.system.aspect;

import lombok.Data;


public enum AuthPowerActionType {
    ADD(1),// 新增
    UPDATE(2),//更新
    DELETE(3);// 删除

    private Integer value;

    AuthPowerActionType(Integer i) {
        this.value = i;
    }

    public Integer getValue() {
        return value;
    }



}
