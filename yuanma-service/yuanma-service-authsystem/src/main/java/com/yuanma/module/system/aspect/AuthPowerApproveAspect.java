package com.yuanma.module.system.aspect;

import com.alibaba.fastjson.JSON;
import com.yuanma.module.system.entity.AuthPowerApproveFlowEntity;
import com.yuanma.module.system.service.AuthPowerApproveFlowService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class AuthPowerApproveAspect {



    // 是否开启三权分立
    @Autowired
    private AuthSystemProperties properties ;

    @Pointcut("@annotation(com.yuanma.module.system.aspect.YuanmaAuthPower)")
    public void authPowerCut() {}

    @Autowired
    private AuthPowerApproveFlowService authPowerApproveFlowService;

    @Around(value = "authPowerCut()")
    public Object  save(ProceedingJoinPoint proceeding) throws Throwable{
        if(properties.isPowerEnable() && !AuthPowerApproveContextHolder.pop()) {
            MethodSignature signature = (MethodSignature) proceeding.getSignature();
            Method method = signature.getMethod();
            YuanmaAuthPower winnerAuthPower = method.getAnnotation(YuanmaAuthPower.class);
            if (null == winnerAuthPower) {
                return proceeding.proceed();
            }
            log.info("开始三权分立拦截，拦截类={},拦截方法={}",proceeding.getTarget(),method.getName());
            AuthPowerApproveFlowEntity entity = new AuthPowerApproveFlowEntity();
            entity.setTitle(winnerAuthPower.title());
            entity.setReqMethod(method.getName());
            entity.setType(winnerAuthPower.type().getValue());
            entity.setModule(winnerAuthPower.module());
            // 类名
            entity.setReqInstanceClass(proceeding.getTarget().getClass().getName());
            // 数据参数
            Object[] args = proceeding.getArgs();
            List<Object> arguments = new ArrayList<>();
            List<String> argsClzNames = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest
                        || args[i] instanceof ServletResponse
                        || args[i] instanceof MultipartFile
                        || args[i] instanceof MultipartFile[]) {
                    continue;
                }
                if (null != args[i]) {
                    arguments.add(args[i]);
                    argsClzNames.add(args[i].getClass().getName());
                }
            }
            entity.setReqParamsType(JSON.toJSONString(argsClzNames));
            entity.setReqParams(!arguments.isEmpty() ? JSON.toJSONString(arguments) : null);

            entity.setStatus("0");
            authPowerApproveFlowService.save(entity);
            try {
                return convertBean(method.getReturnType());
            }finally {
                log.info("完成三权分立拦截，拦截类={},拦截方法={}",proceeding.getTarget(),method.getName());
            }
        } else {
            return proceeding.proceed();
        }

    }

    private Object convertBean(Class clz){
        try {
            return BeanUtils.instantiateClass(clz);
        } catch(Exception e){

        }
        return null;
    }

}
