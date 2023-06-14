package com.yuanma.conf;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;


import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class AutoFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null) {
            setFieldValByName("createTime",new Date(), metaObject);
        }
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        strictInsertFill(metaObject, "createBy", String.class, obtainUserId());
        strictInsertFill(metaObject, "updateBy", String.class, obtainUserId());
        strictInsertFill(metaObject, "deleted", Integer.class, 0);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        strictInsertFill(metaObject, "updateBy", String.class, obtainUserId());
    }
    public static String obtainUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null != servletRequestAttributes){
            HttpServletRequest request =  servletRequestAttributes.getRequest();
            if(null != request){
                if(request.getAttribute("uid") == null){
                    return "winner";
                }
                return  request.getAttribute("uid").toString();
            }
        }
        return null;
    }


}
