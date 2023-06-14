package com.yuanma.module.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.system.Constants;
import com.yuanma.module.system.aspect.AuthSystemProperties;
import com.yuanma.module.system.entity.AuthPowerApproveFlowEntity;
import com.yuanma.module.system.model.dto.*;
import com.yuanma.module.system.query.DeptQuery;
import com.yuanma.module.system.query.PowerApproveQuery;
import com.yuanma.module.system.query.PowerApproveUpdateQuery;
import com.yuanma.module.system.service.AuthPowerApproveFlowService;
import com.yuanma.module.utils.PageUtil;
import com.yuanma.module.utils.SystemApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Api(tags = "审批管理")
@RestController
@Slf4j
@RequestMapping(Constants.API + "/power/flow")
public class PowerApproveController {

    // 是否开启三员分立
    @Autowired
    private AuthSystemProperties properties ;

    @Autowired
    private AuthPowerApproveFlowService authPowerApproveFlowService;

    @ApiOperation("查询是否开启三员分立")
    @PostMapping("/enable")
    public SystemApiResult enable() {
        return new SystemApiResult(properties.isPowerEnable());
    }

    @ApiOperation("一览")
    @GetMapping
    @YuanmaLog(value = "一览",moudle = "审批管理")
    public SystemApiResult query(PowerApproveQuery powerApproveQuery) throws Exception {
        AuthPowerApproveFlowQueryDto dto = new AuthPowerApproveFlowQueryDto();
        BeanUtils.copyProperties(powerApproveQuery,dto);
        if(StringUtils.isNotEmpty(powerApproveQuery.getStatus())){
            if("0".equals(powerApproveQuery.getStatus())){
                dto.setSstatus(Arrays.stream(new String[]{"0"}).collect(Collectors.toList()));
            }else if("1".equals(powerApproveQuery.getStatus())){
                dto.setSstatus(Arrays.stream(new String[]{"1","2","3"}).collect(Collectors.toList()));
            }
        }
        powerApproveQuery.setSort("id,desc");
        Page page = new Page(powerApproveQuery.getPage(),powerApproveQuery.getSize());
        PageUtil.addOrder(powerApproveQuery.getSort(),"id,desc",page, AuthPowerApproveFlowEntity.class);
        Page<AuthPowerApproveFlowDto> authPowerApproveFlowDtos = authPowerApproveFlowService.queryAll(dto, page);
        return new SystemApiResult(PageUtil.toPage(authPowerApproveFlowDtos));
    }

    @ApiOperation("详细")
    @GetMapping("/{id}")
    @YuanmaLog(value = "详细",moudle = "审批管理")
    public SystemApiResult detail(@PathVariable Long id) throws Exception {
        AuthPowerApproveFlowDto detail = authPowerApproveFlowService.detail(id);
        detail.setReqParams(detail.getReqParams().replaceAll("password","dp1"));
        return new SystemApiResult(detail);
    }


    @ApiOperation("提交")
    @PostMapping("/refer")
    @YuanmaLog(value = "提交",moudle = "审批管理")
    public SystemApiResult refer(PowerApproveUpdateQuery query){
        AuthPowerApproveFlowUpdateDto dto = new AuthPowerApproveFlowUpdateDto();
        BeanUtils.copyProperties(query,dto);
        dto.setStatus("0");
        authPowerApproveFlowService.next(dto);
        return new SystemApiResult("0");
    }

    @ApiOperation("撤消")
    @PostMapping("/revoke")
    @YuanmaLog(value = "撤消",moudle = "审批管理")
    public SystemApiResult revoke(PowerApproveUpdateQuery query){
        AuthPowerApproveFlowUpdateDto dto = new AuthPowerApproveFlowUpdateDto();
        BeanUtils.copyProperties(query,dto);
        dto.setStatus("3");
        authPowerApproveFlowService.next(dto);
        return new SystemApiResult("0");
    }

    @ApiOperation("审批通过")
    @PostMapping("/approve")
    @YuanmaLog(value = "审批通过",moudle = "审批管理")
    public SystemApiResult approve(PowerApproveUpdateQuery query){
        AuthPowerApproveFlowUpdateDto dto = new AuthPowerApproveFlowUpdateDto();
        BeanUtils.copyProperties(query,dto);
        dto.setStatus("1");
        authPowerApproveFlowService.finish(dto);
        return new SystemApiResult("0");
    }

    @ApiOperation("驳回")
    @PostMapping("/reject")
    @YuanmaLog(value = "驳回",moudle = "审批管理")
    public SystemApiResult reject(PowerApproveUpdateQuery query){
        AuthPowerApproveFlowUpdateDto dto = new AuthPowerApproveFlowUpdateDto();
        BeanUtils.copyProperties(query,dto);
        dto.setStatus("2");
        authPowerApproveFlowService.next(dto);
        return new SystemApiResult("0");
    }


}
