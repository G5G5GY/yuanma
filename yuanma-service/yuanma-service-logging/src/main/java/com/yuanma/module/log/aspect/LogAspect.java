package com.yuanma.module.log.aspect;

import com.alibaba.fastjson.JSON;
import com.yuanma.module.log.entity.LogEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect extends AbstractYuanmaAspect{

    @AfterReturning(value = "logPoinCut()",returning = "rt")
    public void saveSysLog(JoinPoint joinPoint,Object rt) {
        saveLog(joinPoint,rt);
    }

    @Override
    protected void fillExtendParams(LogEntity sysLogDto, JoinPoint joinPoint, Object rt) {
        sysLogDto.setJsonResult(JSON.toJSONString(rt));
    }

}
