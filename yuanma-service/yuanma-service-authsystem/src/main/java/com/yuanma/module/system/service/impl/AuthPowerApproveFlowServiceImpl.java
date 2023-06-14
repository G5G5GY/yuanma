package com.yuanma.module.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanma.module.system.aspect.AuthPowerApproveContextHolder;
import com.yuanma.module.system.aspect.YuanmaAuthPower;
import com.yuanma.module.system.entity.ApproveStatus;
import com.yuanma.module.system.entity.AuthPowerApproveFlowEntity;
import com.yuanma.module.system.entity.AuthPowerApproveFlowLogEntity;
import com.yuanma.module.system.mapper.AuthPowerApproveFlowLogMapper;
import com.yuanma.module.system.mapper.AuthPowerApproveFlowMapper;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowDto;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowQueryDto;
import com.yuanma.module.system.model.dto.AuthPowerApproveFlowUpdateDto;
import com.yuanma.module.system.service.AuthPowerApproveFlowService;
import com.yuanma.webmvc.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Service
@DS("auth")
public class AuthPowerApproveFlowServiceImpl implements AuthPowerApproveFlowService {

    private static final Logger logger = LoggerFactory.getLogger(AuthPowerApproveFlowServiceImpl.class);

    @Autowired
    private AuthPowerApproveFlowMapper authPowerApproveFlowMapper;

    @Autowired
    private AuthPowerApproveFlowLogMapper authPowerApproveFlowLogMapper;

    @Override
    public Page<AuthPowerApproveFlowDto> queryAll(AuthPowerApproveFlowQueryDto query, Page page) {
        Page<AuthPowerApproveFlowDto> byCond = authPowerApproveFlowMapper.findByCond(query, page);
        return byCond;
    }

    @Override
    public String nextId() {
        Long nextId = 10000L + authPowerApproveFlowMapper.nextIdByNow();
        return DateUtils.format(new Date(),"YYYYMMDD") + nextId.toString().substring(1);
    }

    @Override
    public int save(AuthPowerApproveFlowEntity entity) {
        entity.setFlowNo(nextId());
        return authPowerApproveFlowMapper.insert(entity);
    }

    /**
     * 审批通过
     */
    @Transactional
    public void finish(AuthPowerApproveFlowUpdateDto dto) {

        AuthPowerApproveFlowEntity authPowerApproveFlowEntity = updateAuthPowerApproveFlowEntity(dto);
        saveAuthPowerApproveFlowLogEntity(authPowerApproveFlowEntity);

        Class clzBean = convertClass(authPowerApproveFlowEntity.getReqInstanceClass());
        // 取得对应的服务类
        Object service = SpringContextHolder.getBean(clzBean);
        // 取得对应的参数类型
        Class[] paramsTypes = batchConvertClass(authPowerApproveFlowEntity.getReqParamsType());
        Method method = getMethod(service.getClass(),
                authPowerApproveFlowEntity.getReqMethod(),paramsTypes);

        DynamicDataSourceContextHolder.push("auth");
        AuthPowerApproveContextHolder.set();

        invoke(service,method,batchConvertParams(authPowerApproveFlowEntity.getReqParams(),paramsTypes,
                getMethod(clzBean, authPowerApproveFlowEntity.getReqMethod(),paramsTypes)));

    }

    @Override
    public AuthPowerApproveFlowDto detail(Long id) {
        AuthPowerApproveFlowDto authPowerApproveFlowDto = authPowerApproveFlowMapper.findById(id);

        List<AuthPowerApproveFlowLogEntity> authPowerApproveFlowLogEntities = authPowerApproveFlowLogMapper
                .selectList(new LambdaQueryWrapper<>(AuthPowerApproveFlowLogEntity.class)
                .orderByDesc(AuthPowerApproveFlowLogEntity::getCreateTime)
                .eq(AuthPowerApproveFlowLogEntity::getFlowId, authPowerApproveFlowDto.getId()));
        authPowerApproveFlowDto.setLogs(authPowerApproveFlowLogEntities);

        return authPowerApproveFlowDto;
    }

    public void next(AuthPowerApproveFlowUpdateDto dto){

        AuthPowerApproveFlowEntity entity =  updateAuthPowerApproveFlowEntity(dto);

        saveAuthPowerApproveFlowLogEntity(entity);
    }

    private void saveAuthPowerApproveFlowLogEntity(AuthPowerApproveFlowEntity flowEntity) {

        if(ApproveStatus.APPROVED.getValue().toString().equals(flowEntity.getStatus())
        || ApproveStatus.REJECTED.getValue().toString().equals(flowEntity.getStatus())) {
            AuthPowerApproveFlowLogEntity entity = new AuthPowerApproveFlowLogEntity();
            entity.setFlowId(flowEntity.getId());
            entity.setApproveStatus(flowEntity.getStatus());
            entity.setRemarker(flowEntity.getRemarker());
            //entity.setOperator(operator);
            authPowerApproveFlowLogMapper.insert(entity);
        }
    }

    private AuthPowerApproveFlowEntity updateAuthPowerApproveFlowEntity(AuthPowerApproveFlowUpdateDto dto){
        AuthPowerApproveFlowEntity authPowerApproveFlowEntity = authPowerApproveFlowMapper.selectById(dto.getId());
        authPowerApproveFlowEntity.setStatus(dto.getStatus());
        authPowerApproveFlowEntity.setRemarker(dto.getRemarker());
        authPowerApproveFlowEntity.setUpdateBy(dto.getUserId());
        authPowerApproveFlowEntity.setUpdateTime(new Date());
        authPowerApproveFlowMapper.updateById(authPowerApproveFlowEntity);
        return authPowerApproveFlowEntity;
    }


    private Method getMethod(Class clz,String methodName,Class<?>... paramTypes){
        Method method = BeanUtils.findDeclaredMethod(clz,
                methodName,paramTypes);
        if(null == method){
            method = getMethod(clz, methodName);
        }
        return method;
    }

    private Method getMethod(Class clz,String methodName){
       Method[] methods =  clz.getDeclaredMethods();
       for(Method method:methods){
           if(method.getName().equals(methodName)){
               return method;
           }
       }
       return null;
    }

    private void invoke(Object ob ,Method method,Object[] params){
        try {
            method.invoke(ob,params);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(),e);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(),e);
        }
    }

    private Class convertClass(String clzName){
        try {
            return Class.forName(clzName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private Class[] batchConvertClass(String clzNames){
        JSONArray array = JSONArray.parseArray(clzNames);
        List<Class> list = new ArrayList<>();
        for(Object arr:array){
            list.add(convertClass(arr.toString()));
        }
        return list.toArray(new Class[]{});
    }

    private Object[] batchConvertParams(String params,Class[] clzNames,Method method){
        List<Object> list = new ArrayList<>();
        if(clzNames.length>0){
            JSONArray array = JSONArray.parseArray(params);
            //method.get;
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            for(int i = 0;i < clzNames.length;i++){
                Object ob = null;
                if(genericParameterTypes.length > i &&  genericParameterTypes[i] instanceof ParameterizedType){
                    ob =   JSONObject.parseObject(JSONObject.toJSONString(array.get(i)),genericParameterTypes[i])  ;
                } else {
                    ob =   JSONObject.parseObject(JSONObject.toJSONString(array.get(i)),clzNames[i])  ;
                }

                list.add(ob);
            }
        }
        return list.toArray(new Object[]{});
    }

    public static void TT(int i,List<String> list,long t,HashSet<Integer> set){


    }

    public static void main(String[] args) {
        AuthPowerApproveFlowServiceImpl service = new AuthPowerApproveFlowServiceImpl();
        Class[] arrs=  service.batchConvertClass("[\"com.yuanma.module.system.entity.DeptEntity\"]");
        System.out.println(arrs);

        //Object[] obs = service.batchConvertParams("[{\"deptSort\":999,\"enabled\":true,\"name\":\"汇纳科技125\",\"subCount\":0}]", arrs);
        //System.out.println(obs);

        Method method = service.getMethod(AuthPowerApproveFlowServiceImpl.class, "TT");
        System.out.println(method.getName());

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        System.out.println(genericParameterTypes);

    }


}
