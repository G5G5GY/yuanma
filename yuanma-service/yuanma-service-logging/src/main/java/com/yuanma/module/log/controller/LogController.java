package com.yuanma.module.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.log.entity.LogEntity;
import com.yuanma.module.log.model.dto.LogQueryDto;
import com.yuanma.module.log.model.vo.LogVO;
import com.yuanma.module.log.query.LogQuery;
import com.yuanma.module.log.service.LogService;
import com.yuanma.module.log.utils.LogApiResult;
import com.yuanma.module.log.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = "审记日志管理")
@RestController
@AllArgsConstructor
@RequestMapping("/api/rest_s/v1/system/" + "logs")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation("新增日志")
    @PostMapping
    public LogApiResult create(HttpServletRequest req, @RequestBody LogVO logVo){
        LogEntity logEntity = new LogEntity();
        BeanUtils.copyProperties(logVo,logEntity);
        logEntity.setCreateTime(new Date());
        logEntity.setStatus("0");
        if(!StringUtils.isEmpty(logEntity.getErrorMsg())){
            logEntity.setStatus("1");
        }
        Long logId = logService.create(logEntity);
        return  new LogApiResult(logId);
    }

    @ApiOperation("日志一览")
    @GetMapping
    public LogApiResult query(HttpServletRequest req, LogQuery logQuery){

        LogQueryDto logQueryDTO  = new LogQueryDto();
        BeanUtils.copyProperties(logQuery,logQueryDTO);
        Page page = new Page(logQuery.getPage(),logQuery.getSize());
        Util.addOrder(logQuery.getSort(),"createTime,desc",page,LogEntity.class);
        Page<LogEntity> pager = logService.findByCond(page,logQueryDTO);
        return  new LogApiResult(Util.toPage(pager));
    }

    @ApiOperation("日志详细")
    @GetMapping("/{logId}")
    public LogApiResult detail(@PathVariable("logId")Long logId){
        LogEntity byId = logService.findById(logId);
        return new LogApiResult(byId);
    }
}