package com.yuanma.webmvc.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 日志信息
 */
@Data
@ToString
public class SysLogDto {
    // 操作模块
    private String title;
    // 业务类型（0其它 1新增 2修改 3删除）
    private Integer businessType;
    // 请求方法
    private String method;
    // 请求方法
    private String requestMethod;
    //操作人员
    private String operName;
    // 请求地址
    private String operUrl;
    // 操作地址
    private String operIp;
    // 操作地点
    private String operLocation;
    // 请求参数
    private String operParam;
    // 返回参数
    private String jsonResult;
    // 错误消息
    private String errorMsg;
    // 操作时间
    private String operTime;
    // 详情主键
    private String detailId;

}
