package com.yuanma.module.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_Login_Log")
public class LoginLogEntity {

    @TableId(value="ID",type= IdType.AUTO)
    @TableField("ID")
    private Long ID;

    private String userName;

    private Date loginTime;

    private String userIP;

    @ApiModelProperty(" 1是成功，0失败")
    private Integer loginResult = 0;

    @ApiModelProperty(" 1是成功，0失败")
    private Integer loginOrginResult = 0;

}
