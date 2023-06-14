//package com.yuanma.module.system.mapper.inject;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class SqlInjector extends DefaultSqlInjector {
//
//    @Override
//    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//        // 这里很重要，先要通过父类方法，获取到原有的集合，不然会自带的通用方法会失效的
//        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        // 添加自定义方法类
//        methodList.add(new ApproveMethod());
//
//        return methodList;
//    }
//
//}
