package com.yuanma.module.log.aspect;

import com.alibaba.fastjson.JSON;
import com.yuanma.module.log.annotation.YuanmaLog;
import com.yuanma.module.log.entity.LogEntity;
import com.yuanma.module.log.service.LogService;
import com.yuanma.webmvc.util.HttpUtil;
import com.yuanma.webmvc.util.IpAddressUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractYuanmaAspect {

    protected  final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    protected LogService sysLogService;

    @Value("${request.log.enable:true}")
    protected boolean isWriteLog = true;

    @Value("${request.log.select.enable:false}")
    protected boolean isSelectWriteLog = true;


    @Pointcut("@annotation(com.yuanma.module.log.annotation.YuanmaLog)")
    public void logPoinCut() {}

    protected abstract void fillExtendParams(LogEntity sysLogDto,JoinPoint joinPoint, Object ob);

    public void saveLog(JoinPoint joinPoint, Object ob) {
        if(!isWriteLog){
            return ;
        }
        try {
            LogEntity logDto = new LogEntity();
            //从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            YuanmaLog winnerLog = method.getAnnotation(YuanmaLog.class);
            if(null == winnerLog){
                return;
            }
            //获取切入点所在的方法
            logDto.setTitle(winnerLog.value());
            logDto.setModule(winnerLog.moudle());
            logDto.setMethod(method.getName());
            logDto.setBusinessType(winnerLog.type().getValue());
            if(!isSelectWriteLog && winnerLog.type().getValue() == 0){
                return;
            }
            //请求的参数
            Object[] args = joinPoint.getArgs();
            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest
                        || args[i] instanceof ServletResponse
                        || args[i] instanceof MultipartFile
                        || args[i] instanceof MultipartFile[]) {
                    continue;
                }
                if(null != args[i]) {
                    arguments.add(args[i]);
                }
            }
            //请求的参数
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(null != servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                //将参数所在的数组转换成json
                String params = JSON.toJSONString( HttpUtil.getBodyParams(request));
                logDto.setOperParam(params);
                logDto.setRequestMethod(request.getMethod());
                logDto.setOperUrl(request.getRequestURI());
                String ipAddress = IpAddressUtils.getIpAddr(request);
                logDto.setOperIp(ipAddress);
                logDto.setUserId(null != request.getAttribute("uid")?request.getAttribute("uid").toString():null);
            }

            if(null != arguments && !arguments.isEmpty()){
                for(int i =0;i < arguments.size();i++){
                    Object argument = arguments.get(i);
                    if(null != argument && argument.toString().contains("password")){
                        arguments.set(i,argument.toString());
                    }
                }
            }
            //将参数所在的数组转换成json
            String params = JSON.toJSONString(arguments);

            logDto.setOperParam(params);
            logDto.setOperTime(new Date());
            logDto.setCreateTime(new Date());
            fillExtendParams(logDto,joinPoint, ob);
            sysLogService.create(logDto);

        } catch (Exception e1) {
            LOG.error(e1.getMessage(),e1);
        }

    }
}
