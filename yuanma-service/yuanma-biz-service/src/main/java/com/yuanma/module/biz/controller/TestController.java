package com.yuanma.module.biz.controller;

import com.yuanma.auth.bean.UserDataScope;

import com.yuanma.module.biz.query.TestQuery1;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.annotation.type.LogActionType;
import com.yuanma.module.security.annotation.AnonymousGetMapping;
import com.yuanma.module.biz.Constants;

import com.yuanma.webmvc.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试模块")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(Constants.API + "/test")
public class TestController {

    // 需要登录
    @ApiOperation("测试1--需要登录")
    @GetMapping("/test1")
    @YuanmaLog(value = "测试1",moudle = "测试管理",type= LogActionType.SELECT)
    public ApiResult query1(@RequestAttribute UserDataScope userDataScope, TestQuery1 query){
        log.info(userDataScope.getData().toString());
        return ApiResult.createData("测试1"+userDataScope.getData().toString());
    }

    @ApiOperation("测试2-没需要登录")
    @AnonymousGetMapping("/test2")
    @YuanmaLog(value = "测试2",moudle = "测试管理",type= LogActionType.SELECT)
    public ApiResult query2( TestQuery1 query){
        return ApiResult.createData("测试2-没需要登录");
    }

}
