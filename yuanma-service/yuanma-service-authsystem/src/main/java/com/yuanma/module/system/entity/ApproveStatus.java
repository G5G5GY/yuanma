package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum ApproveStatus implements IEnum<Integer> {

    SUBMITTED(0, "待提交"),//待提交
    APPROVED(1, "审批通过"),//审批通过
    REJECTED(2, "驳回"),//驳回
    APPROVALPENDING(3, "待审批");//待审批

    @EnumValue
    private int value;

    private String desc;

    ApproveStatus(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
