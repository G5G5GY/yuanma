package com.yuanma.module.log.annotation.type;

public enum LogActionType {
    SELECT(0),//查询
    ADD(1),// 新增
    UPDATE(2),//更新
    DELETE(3);// 删除
    private Integer value;

    LogActionType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
