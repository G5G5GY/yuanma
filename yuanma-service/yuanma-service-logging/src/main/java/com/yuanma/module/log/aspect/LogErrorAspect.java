package com.yuanma.module.log.aspect;

import com.yuanma.module.log.entity.LogEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogErrorAspect extends AbstractYuanmaAspect{

    @AfterThrowing(value = "logPoinCut()", throwing = "e")
    public void saveSysLog(JoinPoint joinPoint, Throwable e) {
        saveLog(joinPoint,e);
    }

    @Override
    protected void fillExtendParams(LogEntity sysLogDto, JoinPoint joinPoint, Object ob) {
        if(ob instanceof Throwable){
            sysLogDto.setErrorMsg(((Throwable)ob).getMessage());
        }
    }
}
