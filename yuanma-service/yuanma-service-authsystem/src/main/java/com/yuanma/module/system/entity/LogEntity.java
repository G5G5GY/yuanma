//package com.yuanma.module.system.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.util.Date;
//
//
//@Data
//@NoArgsConstructor
//@TableName("tb_log")
//public class LogEntity  implements Serializable {
//
//    @TableId(type= IdType.AUTO)
//    private Long id;
//
//    // 系统模块
//    private String module;
//    // 操作模块
//    private String title;
//    // 业务类型（0其它 1新增 2修改 3删除 4.）
//    private Integer businessType;
//    // 请求方法
//    private String method;
//
//    //操作人员
//    private String operName;
//    // 请求地址
//    private String operUrl;
//    // 操作地址
//    private String operIp;
//    // 操作地点
//    //private String operLocation;
//    // 请求参数
//    private String operParam;
//    // 返回参数
//    private String jsonResult;
//    // 错误消息
//    private String errorMsg;
//    // 操作状态
//    private String status;
//    private Date operTime;
//
//    private Date createTime;
//    // 详情主键
//    private String detailId;
//
//
//
//}
